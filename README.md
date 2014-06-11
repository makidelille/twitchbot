# twitchbot

Ecrit par makidelille http://twitter.com/makidelille



## UTILISATION

1. Aller sur http://twitchapps.com/tmi/ et obtenez votre cl� oauth
2. pour lancer le bot : java -jar bot.jar <cl�> <FALSE > ces parametres sont optionels mais il vous seront alors demander au demarage
    * cl� correspond � votre cl� oauth
    * Taper 'FALSE' si vous voulez un message lorsque le bot rejoins le chat  

## COMMANDES ADMIN

* !ping pong
* !delay indique le temps de latence entre deux messages du bot
* !delay <par> d�fini le temps de latence entre deux messages du bot � <par>ms
* !leave quitte le tchat
* !join <par> rejoint le tchat <par>
* !quit arr�te le bot
* !pause met le bot en pause
* !resume sort le bot de sa pause
* !reload recharge l'ensemble des commandes
* !alwaysanswer le bot repondra � tout les messages commen�ant par '!'
* !neveranswer le bot repondra seleument aux commandes
* !addgencmd <cmd> <msg> ajout une commande g�n�rale <cmd> et repondra <msg>
* !delgencmd <cmd> supprime la commande g�n�rale <cmd>

## COMMANDES OP

* !addcmd <cmd> <msg> m�me que !addgencmd mais est sp�cifique au tchat dans lequel le bot est
* !delcmd <cmd> m�me que !delgencmd mais est sp�cifique au tchat dans lequel le bot est
* !randadd <join || leave || sentence> <msg> ajoute <msg> aux bibilioth�ques des phrase al�atoire
* !cancel anule le !votetimeout en cours
* !result affiche le resultat du !votetimeout est fait le necessaire
* !cmdlist affiche la liste des commandes disponibles
* !oplist affiche la liste des Ops disponibles

## COMMANDE COMMUNE

* !time donne l'heure local du bot
* !lien <par> donne un lien pour r�pondre � votre recherche (<par>)
* !random ecrit un message al�atoire
* !random <join || leave> ecrit un message al�atoire de bienvenue ou de depart
* !random <par> si <par> est un entier, donne un nombre al"atoire entre 0 et <par> (exclus)
* !votetimeout <par> <temps> lance un vote afin de timeout <par> pour <temps> secondes
* !votetimeout <par> = !votetimeout <par> 300
* !yes || !oui vote oui
* !no || !non vote non


## SYNTAXE MESSAGE

* @m indique un message de type \me. A mettre au debut du message
* @s indique qu'il faut remplacer @s par le nom de celui qui � taper la commande

## SCRIPT

* script hodor reponds HODOR � toute phrase contenant hodor
* script Xd reponds un truc Xd
