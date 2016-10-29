# TeamUp App
code name: Connect&Play

This app allows users to connect and team up with other players to play sport games.

## Disclaimer
- Sport icons obtained from [Flaticon](http://www.flaticon.com/)
- Material icons from [Google](https://design.google.com/icons/)

## Using the Source Code in Android Studio
1. Clone the repository
  
  ```
  $ git clone https://github.com/maheshgaya/ConnectAndPlay.git
  $ cd ConnectAndPlay/
  ```
2. Import the project in Android Studio. (File -> New -> Import Project)
3. Download your Google Maps API key from the [Google Developer Console](https://console.developers.google.com/). Make sure that you enable `Google Maps API for Android` and `Google Places API for Android`
4. Add your API key to google_maps_api.xml
5. Run the project in an emulator with api greater or equal to 21

## Contributing to the Source Code
1. Fork this repository
2. Make all the changes, or add new features in your **own** repository

  **NOTE: When pushing commit to your repository, uncheck google_maps_api.xml to ignore the api key that you added. This will leave GOOGLE_MAPS_API_KEY as the default.**
3. Once you implement your patch, [rebase](https://github.com/edx/edx-platform/wiki/How-to-Rebase-a-Pull-Request) your repository to reflect the latest commits on this repository
4. Pull a request, once you are ready to submit your patch
