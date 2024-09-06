Project Name: Music Player

The "Music Player" app offers users a seamless and intuitive way to enjoy music stored on their local devices. The app starts with a splash screen displaying the app’s logo and title/motto. Upon entering the main screen, users can navigate between two swipeable screens managed by ViewPager2 and TabLayout.

Features:

Permissions: Users must grant permissions to access and read files from their device storage.

Song List: The first fragment displays all the songs available on the device in a list format. Each song item features:
Song cover,
Song title,
Mini media player button.

When the mini player button is clicked, the respective song plays, and clicking again stops it. If a new song is selected, the previous one stops automatically.

MusicPlayer Activity: Clicking on a song item navigates the user to the full MusicPlayer Activity, where the song plays using ExoPlayer. The user can:

Play, pause, skip to the next or previous track

View the song’s title, artist name, and album details

Add the current song to a new or existing playlist

Shuffle songs or repeat a song/playlist

Directly add the current song to the "Liked Songs" playlist

Background Play: When minimized, the song continues playing via a notification player. Users can control playback (next, previous, stop, play) directly from the notification.

Playlists: The second fragment displays user-created playlists along with a default "Liked Songs" playlist. Clicking on a playlist shows the list of songs it contains. 
Users can: View playlist details like title and number of songs, Each song item features:
Song cover,
Song title,
Delete songs from a playlist

Search: Users can search for songs or playlists using the search feature available in the toolbar on the main screen.

Future Updates:

Sorting songs by title or by the date the song was added to the device.

Incorporating a music waveform feature in the MusicPlayer Activity for a more dynamic UI.

Allowing playlist creation from the PlaylistFragment and providing an easier way to add songs to playlists.

Enabling users to play all songs in a playlist in the order they are displayed.

Technologies Used:

Android: The primary sdk for building the mobile application.
Kotlin: The programming language used with Android.

Design Philosophy:

This app is designed to enhance user experience with a smooth and flexible interface, providing multiple ways to play and manage music. Transitions between screens are made seamless with sliding animations, offering a visually appealing and user-friendly experience.
