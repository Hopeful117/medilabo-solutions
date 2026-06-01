## Qu’est ce que le Green Code ?

Le Green code est une pratique ayant pour objectifs d’introduire des solutions réduisant l’impact écologique dans les cycles de développement de logiciels informatiques.

Cette pratique repose sur 3 axes:

- La logique Green
- Une méthodologie
- Une Platforme de conception

### La logique Green

On peut isoler quatre mécaniques qui animent la logique du Green Coding :

- **Un UX design et un UI orientés** vers les avantages
  et la réponse aux besoins proposés aux utilisateurs, afin de limiter le
  temps d’utilisation des services et matériels ainsi que l’énergie
  employée ;
- **Une suppression systématique des déchets de code**, par la suppression des codes dits « morts », par exemple, via l’utilisation de moteurs d’arborescence ou l’attribution de budgets liés à la taille et/ou la performance des projets, pour les développeurs et
  les porteurs de projets ;
- **L’emploi de ressources ayant une empreinte carbone fai**ble, par exemple par l’usage de formats de fichiers simples et légers, des
  API efficaces ou l’optimisation du conditionnement des images ;
- **Une gestion optimisée de l’utilisation des services**, par exemple via les applications web progressives (PWA) ou les réseaux
  de diffusion de contenu (CDN) qui peuvent gérer l’expiration du
  contenu.

### La méthodologie

La première nécessité est la captation et l’enregistrement d’informations afin de pouvoir prendre les meilleures décisions. Les méthodes Agile ou Lean peuvent par exemple intégrer des critères
d’efficacité utiles ; dans la même lignée, les pratiques d’intégration et de livraison continues permettent de mieux comprendre l’impact de
chaque décision de développement.

La pratique du partage et de la réutilisation des projets et actions de Green Coding est aussi
indispensable, au sein du périmètre de l’organisation comme au-delà.

### La platforme de conception

L’infrastructure qui héberge et permet le fonctionnement du code est
un périmètre aussi important que le code proprement dit. Il est donc
nécessaire de considérer :

- **D’utiliser de manière optimale les systèmes** : une trop faible utilisation, si elle peut paraître bénéfique, résulte et démontre une erreur par surestimation lors des étapes de planification,
  ce qui signifie donc des systèmes plus importants qu’ils ne le devraient ;
- **De configurer au plus précis**, en réalisant un examen préalable précis, pour révéler d’éventuelles inefficacités, comme la compression HTTP2 ou zip qui peuvent ne jamais
  être activées ;
- **Une approche et des mesures holistiques** (complètes), qui doivent comprendre, au-delà du suivi de la demande énergétique des serveurs, l’évaluation des infrastructures dites
  « cachées », comme les terminaux individuels utilisés (mobiles,batteries, laptops, etc.).

## Les principe du green coding

### Un code efficient

Il est  question de concevoir des algorithmes qui peuvent réaliser les tâches qui leur sont allouées via le moins grand nombre possible d’opérations. De même, les structures de données utilisées visent à réduire les besoins en mémoire et en CPU.

### Optimisation des ressources

L’utilisation des ressources gagne à être optimisée sur les sujets d’allocation de mémoire, de communication réseau ou d’entrée/sortie (E/S ou I/O) fichiers.

### Gestion de l’énérgie

Différentes techniques de gestion et de préservation de l’énergie existent, telles que la planification et la répartition de tâches sur une période à faible demande (principe d’heures creuses) ou la mise en veille d’instances inutilisées (scale from zero ou daily clean).

### Gestion des données

Il s’agit d’une dimension stratégique visant à adopter des comportements efficaces de stockage et de lecture des données. Parmi ceux-ci, on compte, par exemple, la déduplication ou la mise en cache dite intelligente.

## Les avantages du green code

Au-delà des avantages pour l’environnement, le Green Coding présente d’autres avantages par nature :

- Simplicité d’architecture, par la limitation des interdépendances,
  et donc une tendance à la limitation des consommations d’énergie ;
- Vitesse de calcul, les objectifs de simplification répondant aussi à des logiciels plus rapides ;
- Frugalité, selon l’objectif de limitation de la consommation de ressources, à long terme ;
- Résilience, en apportant des pratiques tangibles, démontrables et communicables,
  qui peuvent participer à rassurer et convaincre les consommateurs
  sensibles à l’empreinte environnementale et aux engagements des
  marques.

## Deployer les principes du green code en entreprise

Implementer des mesures de monitoring comme SonarQube pendant le développement ou Visual VM pendant l’éxécution.

Proposer un catalogue de bonne pratique pour les équipes de développement.

Définir des objectifs de perfomances, notamment en terme de réduction de l’impact écologique

## L’impact concret sur le projet Medilabo

Dans ce projet, les principes du *green code* sont intégrés dès les phases de conception à travers des choix architecturaux réfléchis. Le recours à une architecture en microservices permet notamment d’optimiser l’utilisation des ressources en apportant une grande flexibilité dans la gestion des charges. Grâce à l’utilisation d’un load balancer, il est possible d’adapter dynamiquement le dimensionnement des services en fonction de la demande, en ne faisant évoluer que les composants réellement sollicités. Cette approche limite la consommation inutile de ressources et contribue ainsi à réduire l’empreinte énergétique globale du système.

On peut également citer l’adoption d’une méthodologie Agile, qui a permis aux équipes de développement de se concentrer sur la production de code utile, efficace et systématiquement testé. Cette approche itérative a favorisé la construction d’une base de code saine et maintenable, sur laquelle les fonctionnalités ont pu être enrichies progressivement en fonction des retours utilisateurs. Elle contribue ainsi à limiter le développement superflu et à optimiser l’utilisation des ressources.

Enfin, le choix de déployer l’application sous forme de conteneurs Docker permet de garantir l’indépendance des services vis-à-vis de leur environnement d’exécution, tout en facilitant leur portabilité et leur reproductibilité. Cette approche favorise une meilleure densité de déploiement et une utilisation plus efficiente des ressources système. En limitant les surcoûts liés aux environnements hétérogènes et aux configurations redondantes, elle contribue indirectement à réduire l’empreinte énergétique globale de l’application.