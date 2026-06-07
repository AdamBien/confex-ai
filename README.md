# confex - Conference Management

Conference management built on Quarkus and MicroProfile. Domain (sessions, speakers) is modeled after [schema.org/Event](https://schema.org/Event) and [schema.org/Person](https://schema.org/Person).

Based on 👉 [quarkus-microprofile](https://github.com/adambien/quarkus-microprofile) template | BCE-structured 👉 [bce.design](https://bce.design) | AI-assisted with 👉 [airails.dev](https://airails.dev)

## Scope

Manages **Calls for Papers (CFPs)** and **talks** for a conference.

**Actors**
- **Organizer** — opens/closes CFPs, reviews submitted talks, accepts or rejects them.
- **Speaker** — submits talks to an open CFP, updates their submissions, views status.

**API** — REST (JSON), exposed by the `service` module.

**Persistence** — in-memory only. State is lost on restart; no database, no JPA. Use CDI `@ApplicationScoped` collections as the store.

**Out of scope (for now)** — authentication/authorization, ticketing, payments, agenda publishing, attendee management, persistent storage.

## Domain Model — schema.org

The domain is aligned with established [schema.org](https://schema.org) types to stay interoperable and avoid reinventing well-known vocabulary:

- [Event](https://schema.org/Event) — base type for conferences, sessions, and tracks (start/end time, location, attendees).
- [BusinessEvent](https://schema.org/BusinessEvent) — specialization for professional/industry conferences.
- [EducationEvent](https://schema.org/EducationEvent) — specialization for workshops, tutorials, and training sessions.
- [Person](https://schema.org/Person) — speakers, attendees, organizers (name, affiliation, contact).
- [Organization](https://schema.org/Organization) — sponsors, hosts, and speaker affiliations.
- [Place](https://schema.org/Place) — physical venue of an event.
- [PostalAddress](https://schema.org/PostalAddress) — structured address of a venue.
- [VirtualLocation](https://schema.org/VirtualLocation) — online/hybrid session location (e.g., stream URL).
- [Schedule](https://schema.org/Schedule) — recurrence and timing patterns for sessions.
- [Audience](https://schema.org/Audience) — intended audience of a session (e.g., level, role).
- [PresentationDigitalDocument](https://schema.org/PresentationDigitalDocument) — slide decks and presentation artifacts.
- [Offer](https://schema.org/Offer) — ticketing and registration offers for events.

## Getting Started

See [AGENTS.md](AGENTS.md#build--test) for build, dev mode, and system test instructions.

## Modules

- [service](service/README.md) - Quarkus service module
- [service-st](service-st/README.md) - System tests for the service module

## Progress Log

### `/microprofile-server create speaker BC`

Generated files:

- [service/src/main/java/airhacks/cai/speakers/package-info.java](service/src/main/java/airhacks/cai/speakers/package-info.java)
- [service/src/main/java/airhacks/cai/speakers/entity/Speaker.java](service/src/main/java/airhacks/cai/speakers/entity/Speaker.java)
- [service/src/main/java/airhacks/cai/speakers/control/Speakers.java](service/src/main/java/airhacks/cai/speakers/control/Speakers.java)
- [service/src/main/java/airhacks/cai/speakers/boundary/SpeakersResource.java](service/src/main/java/airhacks/cai/speakers/boundary/SpeakersResource.java)
- [service-st/src/main/java/airhacks/cai/speakers/boundary/SpeakersResourceClient.java](service-st/src/main/java/airhacks/cai/speakers/boundary/SpeakersResourceClient.java)
- [service-st/src/test/java/airhacks/cai/speakers/boundary/SpeakersResourceIT.java](service-st/src/test/java/airhacks/cai/speakers/boundary/SpeakersResourceIT.java)

### `pick useful attributes from schema.org`

[Speaker](service/src/main/java/airhacks/cai/speakers/entity/Speaker.java) aligned with [schema.org/Person](https://schema.org/Person):

- [name](https://schema.org/name) — required
- [email](https://schema.org/email)
- [jobTitle](https://schema.org/jobTitle)
- [affiliation](https://schema.org/affiliation) — organization name
- [url](https://schema.org/url) — homepage
- [description](https://schema.org/description) — bio

Optional fields are omitted from JSON when `null`; `fromJSON` tolerates their absence. [SpeakersResourceIT](service-st/src/test/java/airhacks/cai/speakers/boundary/SpeakersResourceIT.java) updated to round-trip the full attribute set.

### `/microprofile-server create session BC`

[Session](service/src/main/java/airhacks/cai/sessions/entity/Session.java) modeled after [schema.org/Event](https://schema.org/Event):

- [name](https://schema.org/name) — required (talk title)
- [description](https://schema.org/description) — abstract
- [startDate](https://schema.org/startDate) — ISO-8601 `Instant`
- [endDate](https://schema.org/endDate) — ISO-8601 `Instant`
- [performer](https://schema.org/performer) — the [Speaker](service/src/main/java/airhacks/cai/speakers/entity/Speaker.java) (cross-BC reference, nested JSON)

Generated files:

- [service/src/main/java/airhacks/cai/sessions/package-info.java](service/src/main/java/airhacks/cai/sessions/package-info.java)
- [service/src/main/java/airhacks/cai/sessions/entity/Session.java](service/src/main/java/airhacks/cai/sessions/entity/Session.java)
- [service/src/main/java/airhacks/cai/sessions/control/Sessions.java](service/src/main/java/airhacks/cai/sessions/control/Sessions.java)
- [service/src/main/java/airhacks/cai/sessions/boundary/SessionsResource.java](service/src/main/java/airhacks/cai/sessions/boundary/SessionsResource.java)
- [service-st/src/main/java/airhacks/cai/sessions/boundary/SessionsResourceClient.java](service-st/src/main/java/airhacks/cai/sessions/boundary/SessionsResourceClient.java)
- [service-st/src/test/java/airhacks/cai/sessions/boundary/SessionsResourceIT.java](service-st/src/test/java/airhacks/cai/sessions/boundary/SessionsResourceIT.java)

### `session has speaker`

`Session.performer` promoted from `String` to a real [Speaker](service/src/main/java/airhacks/cai/speakers/entity/Speaker.java) reference — schema.org/performer is a `Person`, so the JSON now nests:

```json
{
  "name": "Effective Java",
  "performer": { "name": "Duke", "affiliation": "Oracle" }
}
```

The cross-BC reference (`sessions` → `speakers`) is explicit: `Session.toJSON()` delegates to `Speaker.toJSON()`, and `Session.fromJSON()` delegates to `Speaker.fromJSON()`. BCE allows direct references between entities of independent BCs.

Touched files:

- [service/src/main/java/airhacks/cai/sessions/entity/Session.java](service/src/main/java/airhacks/cai/sessions/entity/Session.java)
- [service-st/src/test/java/airhacks/cai/sessions/boundary/SessionsResourceIT.java](service-st/src/test/java/airhacks/cai/sessions/boundary/SessionsResourceIT.java)

### `validate speaker`

[Speaker](service/src/main/java/airhacks/cai/speakers/entity/Speaker.java) self-validates in its compact constructor — no `quarkus-hibernate-validator`, no bean-validation annotations:

- `name` required and non-blank
- `email` must contain `@` if present

Invalid input throws `jakarta.ws.rs.BadRequestException`, which JAX-RS auto-maps to HTTP 400 at any call depth. `fromJSON` now reads `name` via the nullable getter so a missing field flows through the validator (clean 400) instead of an opaque 500.

[SpeakersResourceIT](service-st/src/test/java/airhacks/cai/speakers/boundary/SpeakersResourceIT.java) gained two negative tests asserting the 400 response (`missingNameIsRejected`, `malformedEmailIsRejected`), keeping the class at the 3-tests-per-class ceiling.

### `/java-conventions` — drop `private` on helper methods

Updated the composed [`/java-conventions`](https://github.com/AdamBien/airails) skill to explicitly require package-private over `private` for methods and fields, with testability as the stated reason. Applied retroactively to [Speaker](service/src/main/java/airhacks/cai/speakers/entity/Speaker.java) and [Session](service/src/main/java/airhacks/cai/sessions/entity/Session.java): the JSON helpers (`addIfPresent`, `addInstantIfPresent`, `parseInstant`, `parsePerformer`) are now package-private so same-package unit tests can exercise edge cases directly.

Powered by [airhacks.live](https://airhacks.live)
