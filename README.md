# TopStackUsers

## Architecture
The app uses "Clean" MVVM Architecture. I decided to not create separate modules, but imitate
that with separate directories, because creating separate modules would be an overkill for an 
app of this size.

So, the app consists of 3 "layers":

- data
- domain
- ui

Both the `data` and `ui` layers reference the `domain`, but `domain` layer doesn't know anything about 
and `data` and `ui`.

I simulated the "follow" functionality with SharedPrefs. I decided to use SharedPrefs instead of Room, 
cause Room would be an overkill for such small functionality.

## Tech Stack

I'm using: 

- Retrofit
- Hilt
- Coil

I was confused by the "No 3rd party UI frameworks", but I decided it's fine to also
use the DataStore - since it's part of the Android framework. Also, initially I didn't use any
serialization and parsed everything with `org.json` package, but eventually switched to `kotlinx.serialization`
after deciding it's not a UI framework. I hope that's fine.


## UI

To make the list elements stable and avoid recompositions, I wrapped the `List<UserUiModel>` into `UsersList` + added @Immutable.
I've had some issues with Coil causing recomposition of all elements, so I added a `composeCompiler`
to help me track it down. 

All errors related to fetching the users list are simply displayed with "Ooops something went wrong". 
I decided not to add specific errors as the spec said to simply treat all failures for fetching as a
single error. 


## Testing

I covered the app with unit tests. 

Unit tests in the `data` directory use fakes for testing. Lately I prefer to use fakes to avoid
"testing implementation" and have an actual class that can be configured the way I need.

Tests in the `ui` directory use `Mockk`. For some reason I thought that would involve less code, but
it turned out to be the same. Will re-write with fakes later.

I did not inject dispatchers, because all frameworks that I'm using are main-safe. In a production 
app I would inject dispatchers as well. 

I also recently added some screenshot tests with Paparazzi - but that is mainly for "exploration". I don't have 
much experience with screenshot testing yet.