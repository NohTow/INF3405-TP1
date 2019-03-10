# Gestionnaire de fichier (Client & Serveur)
Dans le cadre d'un laboratoire d'INF3405 à Polytechnique Montréal, nous avons réalisé un serveur qui permet à des utilisateurs de stocker leurs fichiers afin de pouvoir les télécharger à distance. Ce projet à été réalisé en Java, sans bibliothèque additionnelle.
Les points principaux de ce projet sont : 
- La création d'un serveur multithreadé (un thread par connexion)
- La vérification des entrées utilisateurs (adresse IP et port corrects, commande valide...)
- La communication au travers des sockets
- La gestion d'un système d'authentification et le rattachement d'un compte à un dossier utilisateur
- Expérience utilisateur intuitive (affichage de messages lors de la réalisation d'une action/message d'erreur pour des mauvaises commandes)
- L'affichage de logs clairs et détaillés au niveau du serveur
- La création de rendus testés et robustes.
## Instructions
### Utiliser le serveur
Vous pouvez trouver le fichier jar du serveur dans le dossier TP1Serveur/out/artifacts/Serveur/Serveur.jar. Il suffit ensuite de lancer le serveur (java -jar Serveur.jar). Il vous sera ensuite demandé de rentrer l'adresse IP publique du serveur, ainsi que le port sur lequel les clients viendront se connecter (doit être compris entre 5000 et 5050).
La base de données (Database.txt) sera créé automatiquement, ainsi que les différents dossiers lors de la création d'un compte utilisateur.
### Utiliser le client
#### Lancer le client et se connecter
Vous pouvez trouver le fichier jar du client dans le dossier INF3405-TP1/Client/out/artifacts/Client/Client.jar. Il suffit ensuite de lancer le client (java -jar Client.jar). Il vous sera ensuite demandé de rentrer l'adresse IP publique du serveur, ainsi que le port sur lequel le serveur écoute (doit être compris entre 5000 et 5050).
Il faut ensuite rentrer un nom d'utilisateur. Si l'utilisateur n'existe pas, on lui demande de choisir son mot de passe, son espace de stockage est créé et il est désormais connecté. S'il existe, l'utilisateur va pouvoir rentrer son mot de passe (3 tentatives maximum). Une fois le mot de passe correctement entré, l'utilisateur à désormais accès à son espace de stockage
#### Commandes 
Les commandes disponibles sont les suivantes : 
- upload [nomdefichier] (permet d'upload le fichier "nomdefichier" sur le serveur)
- ls (permet de faire la liste des fichiers présent dans le répertoire de l'utilisateur)
- download [nomdefichier] (permet de télécharger le fichier "nomdefichier" vers l'ordinateur du client)
- delete [nomdefichier] (permet de supprimer un fichier présent sur le serveur)
- exit (permet la déconnexion)

## Améliorations possibles
- Hashage des mots de passe
- Gestions des sous-dossiers (cd)
