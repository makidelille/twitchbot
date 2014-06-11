# TWITCHBOT

Ecrit par makidelille http://twitter.com/makidelille.



## UTILISATION

1. Aller sur http://twitchapps.com/tmi/ et obtenez votre clé oauth.
2. pour lancer le bot : java -jar bot.jar [clé] [FALSE] ces parametres sont optionels mais il vous seront alors demander au demarage.
    * clé correspond à votre clé oauth.
    * Taper 'FALSE' si vous voulez un message lorsque le bot rejoins le chat.

## COMMANDES ADMIN

* !ping pong.
* !leave quitte le tchat.
* !join [par] rejoint le tchat [par].
* !quit arréte le bot.
* !pause met le bot en pause.
* !resume sort le bot de sa pause.
* !reload recharge l'ensemble des commandes.
* !alwaysanswer le bot repondra à tout les messages commençant par '!' .
* !neveranswer le bot repondra seleument aux commandes.
* !addgencmd [cmd] [msg] ajout une commande générale [cmd] qui repondra [msg].
* !delgencmd [cmd] supprime la commande générale [cmd].

## COMMANDES OP

* !addcmd [cmd] [msg] même que !addgencmd mais est spécifique au tchat dans lequel le bot est.
* !delcmd [cmd] même que !delgencmd mais est spécifique au tchat dans lequel le bot est.
* !randadd [join || leave ] [msg] ajoute [msg] aux bibiliothèques des phrase aléatoire.
* !cancel anule le !votetimeout en cours.
* !result affiche le resultat du !votetimeout est fait le necessaire.
* !spam active l'antispam avec les paramétre précdant par défault = 60s
* !spam [on || off || set] [temps] active/desactive l'antispam avec [temps]s entre chaque reponse du bot
* !cmdlist affiche la liste des commandes disponibles.
* !oplist affiche la liste des Ops disponibles.

## COMMANDE COMMUNE

* !time donne l'heure local du bot.
* !lien [par] donne un lien pour répondre à votre recherche ([par]).
* !random ecrit un message aléatoire.
* !random [join || leave] ecrit un message aléatoire de bienvenue ou de depart.
* !random [par] si [par] est un entier, donne un nombre al"atoire entre 0 ,inclu, et [par] ,exclu.
* !votetimeout [par] [temps] lance un vote afin de timeout [par] pour [temps] secondes.
* !votetimeout [par] = !votetimeout [par] 300.
* !yes || !oui vote oui.
* !no || !non vote non.


## SYNTAXE MESSAGE

* @m indique un message de type \me. A mettre au debut du message.
* @s indique qu'il faut remplacer @s par le nom de celui qui a taper la commande.

## SCRIPT

* script hodor reponds HODOR à toute phrase contenant hodor.
* script Xd reponds un truc Xd.
