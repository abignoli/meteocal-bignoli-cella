

// -------------------------------------------------------------- SIGNATURES -------------------------------------------------------------------



// The user entity
sig User {
	access: one AccessData,
	calendar: one Calendar
}

// Just to represent paramethers used as unique identifier for the user. In this case, just the username is enough
sig AccessData {}

// The calendar associated to a user
sig Calendar {
	eventsInCalendar: set Event
}

// Just to represent paramethers used as unique identifier for the event
sig EventData{}

// The location where an event can take place
sig Location{}

// Indoor/Outdoor event
abstract sig LocationType {}
one sig Outdoor extends LocationType {}
one sig Indoor extends LocationType{}

// A value describing a specific point in time
sig DateTime{}

// A generic order relationship set of values
abstract sig OrderRelation {}
one sig Smaller extends OrderRelation {}
one sig Equal extends OrderRelation {}
one sig Greater extends OrderRelation {}

// The chronological order of DateTime values
one sig ChronologicalOrdering {
	order: DateTime -> DateTime -> OrderRelation
} 

// The entity representing a generic event
sig Event {
	creator: one User,
	invited: set User,
	participants: some User,
	data: one EventData,
	location: one Location,
	locationType: one LocationType,
	start: one DateTime,
	end: one DateTime,
	badWeatherConditions: set WeatherCondition,
	weatherForecasts: set WeatherForecast
} {
	ChronologicalOrdering.order[end,start] = Greater
}

// A private event is different from an event only due to the fact that only invited users can participate (not considering the creator)
sig PrivateEvent extends Event {}

// An invitation to an event
sig Invitation {
	seen: one boolean,
	event: one Event,
	invited: one User,
	invitationDateTime: one DateTime
}{
	ChronologicalOrdering.order[invitationDateTime,event.end] = Smaller
}

// A generic notification for an event
abstract sig Notification {
	recipient: one User,
	seen: one boolean,
	aboutEvent: one Event,
	notificationDateTime: DateTime
}{
	ChronologicalOrdering.order[notificationDateTime,aboutEvent.end] = Smaller
}


// Will be automatically generated only if any substancial event data changes
sig ChangeNotification extends Notification {}

// Will be automatically generated only if the weather forecast for a specific event changes between good and bad, to warn the user
sig WeatherNotification extends Notification{}

// The entity representing the external weather forecast service we are going to use
// This service will offer, via its APIs, a function taking as input a DateTime value and a location to return a forecast if avaible
sig WeatherForecastService {
	forecast: Location -> DateTime -> lone WeatherCondition
}

// A weather forecast for the time interval (forecastStart, forecastEnd), in the location of the event this weather forecast is associated to
sig WeatherForecast {
	weatherCondition: lone WeatherCondition,
	forecastStart: one DateTime,
	forecastEnd: one DateTime
} {
	ChronologicalOrdering.order[forecastEnd,forecastStart] = Greater
}

// A utility object representing the last weather forecast observed by the user on a specific event
sig WeatherView {
	lastSeen: one WeatherCondition,
	event: one  Event,
	viewer: one User
}

// The different weather conditions, more may be added in the actual implementation
abstract sig WeatherCondition {}
one sig Sun extends WeatherCondition {}
one sig Clouds extends WeatherCondition {}
one sig Rain extends WeatherCondition {}
one sig Snow extends WeatherCondition {}

// Standard boolean value
abstract sig boolean {}
one sig True extends boolean {}
one sig False extends boolean {}



// ----------------------------------------------------------------- FACTS -----------------------------------------------------------------------


// -- USER --


//	No duplicate user
fact noDuplicateUser {
	no disj u1,u2: User | u1.access = u2.access
}

// No access data without any user
fact accessDataBelongsToUser {
	no ad: AccessData | no u: User | u.access = ad
}


// -- CALENDAR --



// Calendars not to be shared between users
fact calendarIsNotShared {
	all c: Calendar | one u: User | c = u.calendar
}

// Conformity between calendar and events its owner decided to participate in
fact calendarConformity {
	all e: Event, u: User | e in u.calendar.eventsInCalendar iff u in e.participants
}

// -- EVENT --


// No shared event data
fact noSharedEventData {
	(no disj e1,e2: Event | e1.data = e2.data)
}

// No event data not associated to an event
fact noUnlinkedEventData {
	no ed: EventData | no e: Event | e.data = ed
}


// -- INVITATION --


// All invited users receive an invitation
fact invitationForInvited {
	all e: Event, u: User | u in e.invited implies one i: Invitation | i.event = e and i.invited = u
}

// No invitation is sent to non invited users
fact noInvitationForNonInvited {
	(no i: Invitation | i.invited not in i.event.invited)
}

// The aren't multiple invitation to the same event for the same user
fact noMultipleInvitations {
	(no disj i1, i2: Invitation | i1.event = i2.event and i1.invited = i2.invited)
}

// The creator of an event can't invite himself to the event
fact creatorCannotInviteHimSelf {
	(no e: Event | e.creator in e.invited)
}


// -- PARTICIPANT --


// All the participants to a private event must be invited first, exception made for the creator
fact participantsMustBeInvitedAtPrivateEvent {
	(no e: PrivateEvent | some u: User | u in e.participants and !(u in e.invited) and e.creator != u)
}

// The creator of an event is always in participants's list
fact creatorInParticipantsList {
	all e: Event | e.creator in e.participants
}

// Notifications should be sent only to participants
fact notificationsOnlyToParticipants {
	(no n: Notification | n.recipient not in n.aboutEvent.participants)
}


// -- WEATHER FORECAST --


// We are going to use only one weather forecast service
fact oneWeatherForecastService {
	#WeatherForecastService = 1
}

//All weather forecasts are relative to only an event
fact weatherForecastBelongToAnEvent {
	all wf: WeatherForecast | one e: Event | wf in e.weatherForecasts
}

// All weather forecasts and are taken by the forecast service
fact weatherForecastFromForecastService {
	all wf: WeatherForecast | one e: Event | wf in e.weatherForecasts and wf.weatherCondition = WeatherForecastService.forecast[e.location,wf.forecastStart]
}

// All weather forecasts relative to an event form a chronological sequence, starting at the event's starting time and ending at the event's ending time
fact weatherForecastInSequence {
	all e: Event | one wf: e.weatherForecasts | wf.forecastStart = e.start 
	all e: Event | one wf: e.weatherForecasts | wf.forecastEnd = e.end
	all wf1: WeatherForecast | one e: Event | wf1 in e.weatherForecasts and (wf1.forecastStart != e.start iff (one wf2: WeatherForecast | wf2 in e.weatherForecasts and wf2.forecastEnd = wf1.forecastStart and wf2.weatherCondition != wf1.weatherCondition))
	all wf1: WeatherForecast | one e: Event | wf1 in e.weatherForecasts and (wf1.forecastEnd != e.end iff (one wf2: WeatherForecast | wf2 in e.weatherForecasts and wf2.forecastStart = wf1.forecastEnd and wf2.weatherCondition != wf1.weatherCondition))
}

// One weather view for (user,event) pair
fact univoqueWeatherViewForUserEvent {
	no disj wv1,wv2: WeatherView | wv1.viewer = wv2.viewer and wv1.event = wv2.event 
	all e: Event, u: User | (u in e.participants) implies one wv: WeatherView | wv.event = e and wv.viewer = u 
}

// No weather view on events the user is not participating in
fact noWeatherViewForNonParticipants {
	no wv: WeatherView | wv.viewer not in wv.event.participants 
}


// -- ADDITIONAL CONSTRAINTS --


// No location not associated to an event
fact noUnlinkedLocation {
	no l: Location | no e: Event | e.location = l
}

// No dateTime not associated to something
fact noUnlinkedDateTime {
	no dt: DateTime | (no e: Event | e.start = dt or e.end = dt) and (no wf: WeatherForecast | wf.forecastStart = dt or wf.forecastEnd = dt) and (no n: Notification | n.notificationDateTime = dt) and (no i: Invitation | i.invitationDateTime = dt)
}

// Chronological ordering properties (general total ordering relationship)
fact chronologicalOrderingProperties {
	all dt: DateTime| ChronologicalOrdering.order[dt,dt] = Equal
	all disj dt1, dt2: DateTime | (ChronologicalOrdering.order[dt1,dt2] = Smaller) iff (ChronologicalOrdering.order[dt2,dt1] = Greater)
	// Transitivity
	all dt1,dt2,dt3: DateTime | (ChronologicalOrdering.order[dt2,dt1] = Greater and ChronologicalOrdering.order[dt3,dt2] = Greater) implies  (ChronologicalOrdering.order[dt3,dt1] = Greater)
}



// ------------------------------------------------------------- PREDICATES ---------------------------------------------------------------------



// No invitation for user that don't belong to the invited list
assert invitationConsistency {
	no i: Invitation | i.invited not in i.event.invited
	#Invitation = (sum e: Event | #e.invited)
}

check invitationConsistency for 5 

// Since user that haven't been invited to a private event cannot partecipate to the event they won't receive any notification from it
assert noNotificationsAboutPrivateEventsForNonInvited {
	no n: Notification | n.recipient not in n.aboutEvent.invited and n.recipient != n.aboutEvent.creator and n.aboutEvent in PrivateEvent
}

check noNotificationsAboutPrivateEventsForNonInvited for 5

// The association between user and access must be bi-directional
assert equalNumberAccessDataUser {
	#User = #AccessData
}

check equalNumberAccessDataUser for 8

//Each event data is associated to one and only one event, and each event has associated data
assert equalNumberEventDataEvent {
	#Event = #EventData
}

check equalNumberEventDataEvent for 8

// The locations represented on the system are no more than the events
assert locationNumberSmallerThanEvents {
	#Location <= #Event
}

check locationNumberSmallerThanEvents for 8

// Checks weather forecasts consistency
assert weatherForecastConsistency {
	all wf: WeatherForecast | one e: Event | wf in e.weatherForecasts and WeatherForecastService.forecast[e.location,wf.forecastStart] = wf.weatherCondition
}

check weatherForecastConsistency for 8

// Shows public event participation constraints
pred showPublicEventParticipationConstraints() {
	#Event = 1
	#PrivateEvent = 0
	#User = 4
	#Notification = 0
	#WeatherForecast = 1
	#Location = 1
	#DateTime = 2
	#Invitation = 2

	all e: Event | #e.participants = 3 and some u: User | u in e.invited and u not in e.participants
}

run showPublicEventParticipationConstraints for 5


// Shows private event participation constraints
pred showPrivateEventParticipationConstraints() {
	#Event = 1
	#PrivateEvent = 1
	#User = 4
	#Notification = 0
	#WeatherForecast = 1
	#Location = 1
	#DateTime = 2
	#Invitation = 2

	all e: Event | #e.participants = 2 and some u: User | u in e.invited and u not in e.participants
}

run showPrivateEventParticipationConstraints for 5

// Show weather forecasts consistency
pred showWeatherForecastConsistency() {
	#Event = 1
	#PrivateEvent = 0
	#User = 1
	#Notification = 0
	#WeatherForecast = 1
	#Location = 1
	#DateTime = 2

	all e: Event | #e.badWeatherConditions = 0
}

run showWeatherForecastConsistency for 5

// Shows weather forecasts sequence properties
pred showWeatherForecastSequenceProperties() {
	#Event = 1
	#PrivateEvent = 0
	#User = 1
	#Notification = 0
	#WeatherForecast = 3
	#Location = 1

	all e: Event | #e.badWeatherConditions = 0
}

run showWeatherForecastSequenceProperties for 5






































