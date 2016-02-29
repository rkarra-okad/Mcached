# Mcached

Il n’y a rien de plus simple qu’un framework de cache … enfin il paraît. Cet article décrit l’installation et l’utilisation du cache distribué Memcached.

Introduction

Memcached un cache mémoire distribué et non répliqué :

    Il permet de stocker des objets sous la forme clé/valeur
    Ces objets peuvent être répartis (ie. distribués) sur un nombre variable d’instances, de serveurs. Ainsi chaque instance sera responsable d’un certain nombre de données
    Ces données ne sont ni persistantes, ni répliquées entre les différentes instances. Formulé autrement, si une des instances tombent, les données qui étaient hébergées par cette instance sont perdues et le code applicatif devra à nouveau solliciter la source (la base de données par exemple…) pour récupérer les dernières valeurs.


Memcached se différencie de plus d’une solution de cache classique type ehcache dans sa mise en œuvre plus que dans ces APIs. 

Mais memcached a été pensé pour être distribué et ainsi être capable de stocker des volumes importants de données, en tout cas des volumes de données supérieurs à ce qu’il est possible de stocker en RAM. Ainsi :

    Chacune des instances a la responsabilité d’un espace de données particulier. Il ne s’agit en revanche pas de partitionner fonctionnellement les données (par exemple, les clients sont stockés sur ce serveur, les contrats sur tel autre serveur…) mais plutôt de répartir techniquement (selon les clés) les données. Ainsi les données de 1-10 seront sur le premier serveur, les données de 11 à 20 sur le second serveur et ainsi de suite.
    Pour répondre à des évolutions de volumétrie (ponctuelles ou régulières) memcached permet de rajouter, ou d’enlever dynamiquement des instances.
    

Installation

Sous centos7

1.Installation avec yum

yum -y install memcached

2.Configuration

éditer le fichier /etc/sysconfig/memcached et modifier les informations du cache

vim /etc/sysconfig/memcached

--------------------------------------- 
- PORT="11211"
- USER="memcached"
- MAXCONN="1024"
- CACHESIZE="512"
- OPTIONS= ""
--------------------------------------
 
3.Quelques commandes de base

- systemctl restart memcached

- systemctl enable memcached

- systemctl status memcached

- systemctl stop memcached

Sous Windows

  1) Télécharger la version memcached 1.4.4-14.
  2) Dézipper le fichier téléchargé.
  3) En ligne de commande, taper.
  
------------------------------------------
c:/memcached/memcached.exe -p 900 -m 512
c:/memcached/memcached.exe -p 800 -m 512 -vvv (lancement avec des logs)
----------------------------------------

Memcached et la Sécurité ?

Par défaut, memcached ne propose aucune fonctionnalité autour de la sécurité. Ainsi,

    - il n’existe pas de mécanismes d’authentification. Limiter les accès extérieurs doit donc se faire, si nécessaire, au niveau réseau (Firewall + filtrage IP)
    - les données ne sont pas encryptées. Est-ce nécessaire ou pas de les crypter est un débat à part entière dans lequel je ferai bien attention de ne pas me lancer. Reste que memcached ne fait rien à ce niveau et que c’est donc à la charge des applicatifs clients.

Intégration de okad-mcached

ce module représente une surcouche d’abstraction à l’API spymemcached java.

Pour utiliser ce dernier, il suffit d’ajouter la dépendance okad-mcache et annoter les méthodes avec l’annotation @Distcacheable.

Voici quelques exemples d’utilisation :

--------------------------------------------------------------------------------
@DistCacheable(name ="userCache", keys = { 0 }, ttl = 60)
public User findByUsername(String username) {
   ...
}
 
@DistCacheable(name ="countryCache", keys = { 0 }, ttl = 3600)
public Iterable<CountryRef> getCountries(String query, String... relations) {
  ...
}
 
// D'autres Annotations
@DistCacheEvict(name = "userCache", keys = { 0 })
@DistCacheEvict(name = "userCache", AllEntries = true)
 
@DistCachePut(name ="userCache", keys = { 0 })
 
---------------------------------------------------------------------------------
name : nom du cache,
keys : l’index des paramètres de la méthode annotée à ajoutés dans la clé du cache
ttl : time-to-live
