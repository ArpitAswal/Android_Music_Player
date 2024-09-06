Project Name: Music Player

The "Music Player" app offers users a seamless and intuitive way to enjoy music stored on their local devices. The app starts with a splash screen displaying the app’s logo and title/motto. 

![Screenshot_2024-09-06-22-53-29-187_com example playmusic](https://github.com/user-attachments/assets/ee33f4ad-9d1e-43f1-a578-14a645e84687)

Upon entering the main screen, users can navigate between two swipeable screens managed by ViewPager2 and TabLayout.

Features:

Permissions: Users must grant permissions to access and read files from their device storage.

Song List: The first fragment displays all the songs available on the device in a list format. Each song item features:
Song cover,
Song title,
Mini media player button.

When the mini player button is clicked, the respective song plays, and clicking again stops it. If a new song is selected, the previous one stops automatically.

https://github.com/user-attachments/assets/c6a02a71-57db-4682-ae89-b5380ccb433e

MusicPlayer Activity: Clicking on a song item navigates the user to the full MusicPlayer Activity, where the song plays using ExoPlayer. The user can:

Play, pause, skip to the next or previous track

View the song’s title, artist name, and album details

Add the current song to a new or existing playlist

Shuffle songs or repeat a song/playlist

Directly add the current song to the "Liked Songs" playlist

![Screenshot_2024-09-06-22-55-38-382_com example playmusic](https://github.com/user-attachments/assets/6351cf8e-e85c-4853-a9c1-e12426adc2ee)

![Screenshot_2024-09-06-22-55-48-507_com example playmusic](https://github.com/user-attachments/assets/00faef30-2657-4b1b-a1c7-8c12233c3272)

![Screenshot_2024-09-06-22-56-06-148_com example playmusic](https://github.com/user-attachments/assets/d0c03700-9e9d-4a8d-99ab-918904fff7a0)

Background Play: When minimized, the song continues playing via a notification player. Users can control playback (next, previous, stop, play) directly from the notification.

https://github.com/user-attachments/assets/a1dbaab3-04a2-4fa6-990e-5b1fc94d5a94

Playlists: The second fragment displays user-created playlists along with a default "Liked Songs" playlist. User also can remove whole playlist with its songs

![Screenshot_2024-09-06-22-56-23-585_com example playmusic](https://github.com/user-attachments/assets/22489cf2-8577-471a-a4c7-2caf482f6abf)

Clicking on a playlist shows the list of songs it contains. 
Users can: View playlist details like title and number of songs, Each song item features:
Song cover,
Song title,
Delete songs from a playlist

![Screenshot_2024-09-06-22-56-33-545_com example playmusic](https://github.com/user-attachments/assets/3d9faedf-54f1-461c-ac97-55f29bbeaabc)

Search: Users can search for songs or playlists using the search feature available in the toolbar on the main screen.

https://github.com/user-attachments/assets/e2a0605f-f585-4745-b1a3-8d9dc8ab1ae1

Future Updates:

Sorting songs by title or by the date the song was added to the device.

Incorporating a music waveform feature in the MusicPlayer Activity for a more dynamic UI.

Allowing playlist creation from the PlaylistFragment and providing an easier way to add songs to playlists.

Enabling users to play all songs in a playlist in the order they are displayed.

Set the Playlist folder image, by default it display headphones 

Technologies Used:

Android: The primary sdk for building the mobile application.
Kotlin: The programming language used with Android.

Design Philosophy:

This app is designed to enhance user experience with a smooth and flexible interface, providing multiple ways to play and manage music. Transitions between screens are made seamless with sliding animations, offering a visually appealing and user-friendly experience.

