# smartrix-21-22-soa-21-22-j


Lorsque vous lancez le `prepare.sh` toutes les images nécessaires seront construites. Le script prepare.sh utilise `curl` et `tar`. Il télécharge également le JDK 17 et Maven 3.8.3. Le script prendra donc un certain temps en fonction de la connexion. 

Lorsque vous lancez le `run.sh` celui-ci lancera avec `docker-compose` l'ensemble des services et leurs dépendances (broker de messages RabbitMQ, Kafka, Zookeeper et base de données MongoDB ).  En plus de ses services il lancera également un conteneur contenant un ensemble de scripts shell et Python qui vont initialiser et dérouler les scénarios. Ce script va simuler l'horloge, l'interface utilisateur, et une station de recharge. Le script dure 20 mins environ (sur Github Actions). Tous les containers s'arrêtent d'eux même une fois que le runner arrive en fin d'exécution. Les scénarios  devrait déclencher des logs des différents services impliqués. Les logs des différents services sont dans l'ordre d'exécution l'un par rapport à l'autre mais ils ne sont pas dans l'ordre par rapport au runner. Il est possible que vous ayez à remonter pour voir le déroulement.

**Attention**

Le `run.sh` supprime TOUS les containers et TOUS les volumes sur la machine : on l'a fait ainsi pour ne pas avoir à lister tous les containers utilisés et pour nettoyer l'environnement d'exécution pour les run suivants. Vous devez commentez les lignes 10 et 12 dans run.sh pour éviter cela.


Pour pouvoir tester et faire la démo nous avons rajouté dans la plupart de nos services un composant qui nous permet de mocker la date courante.


Les services:
- teamj-mongo : instance de MongoDB
- teamjrabbitmq : instance de RabbitMQ (broker de message)
- accounting : service comptabilité
- bank : le mock de la banque
- customerservice :  service client de SmartrixGrid
- localproductionmonitor: service de SmartrixGrid chargé de suivre la production locale d'électricité des clients
- energymonitorservice : service de SmartrixGrid
- jydrosupplier :  instance d'un fournisseur d'énergie
- jcoalsupplier : un autre fournisseur d'énergie
- energyreserve: service chargé de gérér les batteries de Smartrix Grid
- supplierorchestrator: service chargé de piloter les fournisseurs d'énergie pour adapter la production d'énergie à la consommation
- emailservice: service chargé d'envoyer des emails

## REPARTITIONS DES POINTS :

400 points répartis sur 4

ANAGONOU Patrick 100 points
ANIGLO Jonas Vihoalé 100 points
Soulaiman Zabourdine 100 points
FRANCIS Anas 100 points
