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

Powered by [airhacks.live](https://airhacks.live)
