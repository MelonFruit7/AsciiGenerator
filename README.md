# AsciiGenerator
A cool video to ascii-video generator

Flags (Should be used with run.sh)
-----------------------------------------------------------------------------------------------------------------------------------
If you want a new video made:

``-fps {fps for video}`` (An example input would be numbers 1-60) (Used for video flag and for generator) 
- Determines how many frames are extracted per second of the video, this flag needs to be behind the -video flag (by default this flag is set to 20)
- This is used to determine the default speed, so if you're triggering the generator you should make sure to have this flag.

``-wav`` (Used as player flag and for video flag)
- If put behind video flag it will create a sound file for your video called newSound.wav and play it once the video loads.
- It can be used without the video flag to play newSound.wav when the player runs.

``-video {path to video}`` (Used as standalone)
- This specifies to the program to extract a video into frames using ffmpeg, if you don't have ffmpeg you should get it working on your system.
- Using this flag will trigger the generator.
-----------------------------------------------------------------------------------------------------------------------------------
``The generator is what generates the required ascii for the player``

If you want to edit the output (using any of this commands will trigger the generator): 

``-fw {width}`` (Used as a flag for the generator)
- This allows you to change the width of the frames in ascii, the height will be maintained by the aspect ratio of each frame. (fw stands for frame width, by default this is 50)

``-precision {1-225}`` (Used as a flag for the generator)
- This changes appends spaces to the ascii list, this makes more spaces show up when the video is played. In rare cases a high precision is needed, a value of 30 should suit normal videos. 

``-invert`` (Used as a flag for the generator)
- Inverts the ascii list for the player
-----------------------------------------------------------------------------------------------------------------------------------
Used for the ascii player

``-speed {milliseconds}`` (Used as a flag for the player)
- This determines the speed of the ascii player (by default this attempts to set a speed that matches the actual video speed) 
