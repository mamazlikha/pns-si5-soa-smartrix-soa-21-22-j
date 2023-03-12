#language:fr
Fonctionnalité: Scénario 1 Consommation d'énergie par un composant
  Cette fonctionnalité concerne le scenario pendant lequel un composant électrique enregistre
  la quantité consomé

  Contexte:
    Etant donné que la liste de consommation de "Pierre & Marie" est vide

  Scénario: Un composant électrique de l'utilisateur donné qui consomme 24 KW 16 KW 20 KW le 11/01/2021
  la consommation de l'utilisateur donné pour la journée du 01/01/2021 est de 60 KW

    Quand je consomme 24 KW le "01-01-2021"
    Et je consomme 16 KW le "01-01-2021"
    Et je consomme 20 KW le "01-01-2021"
    Et la consommation de "Pierre & Marie" est demandée
    Alors la consommation de Pierre & Marie pour la journée du "01-01-2021" est de 60 KW
    Et la liste des consommation de Pierre & Marie pour la journée du "01/01/2021" est de 24 KW 16 KW et 20 KW