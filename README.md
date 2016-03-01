# Mcached

Il n’y a rien de plus simple qu’un framework de cache … enfin il paraît. Cet article décrit l’installation et l’utilisation du cache distribué Memcached.


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


Assi il faut activé l'OAP dans le context Spring

---------------------------------------------------
<aop:aspectj-autoproxy proxy-target-class="true" />
---------------------------------------------------
