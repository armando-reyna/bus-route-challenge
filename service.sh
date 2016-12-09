#!/bin/bash

# Keep the pwd in mind!
RUN="java -jar target/bus-route-challenge-0.1.0.jar"
NAME=bus
ARGS_NO=$#

if [ $ARGS_NO -lt 1 ] 
	then
    echo "Usage: $0 {start|stop|block} DATA_FILE"
	exit
else

	DATA_FILE=$2

	PIDFILE=$NAME.pid
	LOGFILE=$NAME.log
	
	start() {
	
		echo $ARGS_NO
		echo "File: $DATA_FILE"
		
		if [ $ARGS_NO -eq 2 ] 
			then
				echo "Starting service..."
				if [ -f $PIDFILE ]; then
					echo "$PIDFILE exists."
					if kill -0 $(cat $PIDFILE); then
						echo 'Service already running' >&2
						return 1
					else
						rm -f $PIDFILE
					fi
				fi
					local CMD="$RUN --filePath=$DATA_FILE &> \"$LOGFILE\" & echo \$!"
					echo $CMD
					sh -c "$CMD" > $PIDFILE
		else
			echo "Usage: $0 {start|stop|block} DATA_FILE"
			exit
		fi

	}

	stop() {
		echo "Stoping process..."
		if [ ! -f $PIDFILE ] || ! kill -0 $(cat $PIDFILE); then
			echo 'Service not running' >&2
			return 1
		fi
		kill -15 $(cat $PIDFILE) && rm -f $PIDFILE
	}


	case $1 in
		start)
			start
			;;
		stop)
			stop
			;;
		block)
			start
			sleep infinity
			;;
		*)
	esac
	
fi