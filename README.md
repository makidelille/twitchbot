twitchbot
=========

twitch chat bot

HOW TO USE
==========
1.go to http://twitchapps.com/tmi/ and get your oauth key
2.edit launch.bat and add the parameters :
	-1 : the bot twitch name
	-2 : oauthkey (with the oauth:)
	-3 : 'false' if you don't want to launch in silent mode


ADMIN COMMANDS
==============
!ping pong
!delay show how much delay between two bot msg
!delay <par> set the delay between two bot msg to <par>ms
!leave leave the stream chat
!join <par> join the stream chat <par>
!quit ends the bot
!pause pause the chat bot
!resume resume the chat bot
!reload reload all the chat cmds
!alwaysanswer bot will answer to all msg strating with '!'
!neveranswer bot will never answer to msg starting with '!' unless it's a command
!addgencmd <cmdname> <msg> add the general command named <cmdname> and displaying the message <msg>
!delgencmd <cmdname> delete the general command name <cmdname>

OPS COMMANDS
============
!addcmd <cmdname> <msg> same as !addgencmd but it's a local add
!delcmd <cmdname> same as !delgencmd but it's a local delete
!randadd <join || leave || sentence> <msg> add the <msg> to one of the random libs
!cancel cancel the votetimeout in progress
!result get the result of the votetimeout and auto timeout the user if neccesary

COMMON COMMANDS
===============
!time give the local time
!lien <par> give you a link to what you asked for (<par>)
!random write a random msg from the random libs
!random <join || leave> write a random <join||leave> msg from the libs
!random <par> if <par> is an integer, give a random number between 0(included) and <par>(exluded)
!votetimeout <par> <time> starts a votetimeout to timeout <par> for <time> seconds
!votetimeout <par> = !votetimeout <par> 300
!yes || !oui votes yes
!no || !non votes no
