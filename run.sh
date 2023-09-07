#!/bin/bash


#-------------------------------------------------------------------------------------------
# 											FLAGS
# -fps {fps for video} (Amount of fps when turning videos into frames)
# -wav (Makes a wav file and plays it with the AsciiPlayer
# -video {video} (video to turn into frames)
#
# -speed {milliseconds} (Playback speed in milliseconds)
# 
# -fw {width} (Width of the frame, height will be maintained with the aspect ratio)
# -precision {1-225} (This one is weird to explain, video will usually look pretty good if you set it around 30)
# -invert (Invert ascii frames)
#------------------------------------------------------------------------------------------- 

i=1; fps=20; makeWav=0; runGenerator=0
generatorFlags=""; playerFlags=""

for input in "$@";
do
    i=$((i+1)) #increment i
    case "$input" in #case statement
		"-fps") #ffmpeg flag
			if [[ ${!i} =~ ^[0-9]+$ ]]; then
				fps=$((${!i})) #Magic (turns into int)
				generatorFlags+=" -fps $fps"
			else
				printf "Input a valid integer for fps!\n"; exit 1 
			fi
		;;
		"-wav") #ffmpeg/player flag
			makeWav=1
			playerFlags+=" -wav videos/newSound.wav" 
		;;
        "-video") #ffmpeg flag
			runGenerator=1 #if user is loading a new video they will need to run generator 

            if command -v ffmpeg; then #Check if ffmpeg command runs
                if [ ! -d frames/ ]; then #Check if frames doesn't exist 
                    mkdir frames 
                fi

                rm frames/*
                ffmpeg -i "${!i}" -vf "fps=$fps" frames/image-%d.png
                if [ ! $? -eq 0 ]; then #Check if the ffmpeg outputed an error code
                    printf "\n\nPlease input a valid video file\n"; exit 1  
                fi
				
				if [ $makeWav -eq 1 ]; then 
                	ffmpeg -i ${!i} -ac 2 -f wav videos/newSound.wav
				fi 				
            else
                printf "Install ffmpeg NOW!!! :()\n"; exit 1
            fi
        ;;
		"-speed") #player flag
			if [[ ${!i} =~ ^[0-9]+$ ]]; then
				playerFlags+=" -s ${!i}"
			else 
				printf "Input a valid integer for speed!\n"; exit 1
			fi
		;;
		"-fw") #generator flag
			runGenerator=1 #This is a generator flag, we can assume user wants to use the generator
			if [[ ${!i} =~ ^[0-9]+$ ]]; then
				generatorFlags+=" -w ${!i}"
			else 
				printf "Input a valid integer for width!\n"; exit 1
			fi
		;;
		"-precision") #generator flag
			runGenerator=1 #This is a generator flag, we can assume user wants to use the generator
			if [[ ${!i} =~ ^(1?[0-9]{1,2}|2[0-1][0-9]|22[0-5])$ ]]; then #wacky regex to check 0-225 because why not
				generatorFlags+=" -p ${!i}"
			else 
				printf "Input a valid integer for percision (1-225), recommended around 30!\n"; exit 1
			fi
		;;
		"-invert") #generator flag
			runGenerator=1
			generatorFlags+=" -i"
		;;
    esac
done

if [ $runGenerator -eq 1 ]; then
	javac AsciiGenerator.java
	java AsciiGenerator $generatorFlags
fi
javac AsciiPlayer.java
java AsciiPlayer $playerFlags
