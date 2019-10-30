package com.org.fplab.liveinfostream.betfair.navigation.models

sealed trait NavigationEntity

final case class NavigationEventEntity(event: NavigationGroup)    extends NavigationEntity
final case class NavigationMarketEntity(market: NavigationMarket) extends NavigationEntity

object NavigationEntity {
  def enumerate(root: NavigationRoot): List[NavigationEntity] = {
    def enumerateNavigationEventType(eventType: NavigationEventType): List[NavigationEntity] =
      eventType.children.map(_.flatMap(enumerateNavigationEventTypeChild)).getOrElse(List.empty)

    def enumerateNavigationEventTypeChild(eventTypeChild: NavigationEventTypeChild): List[NavigationEntity] =
      eventTypeChild match {
        case NetcGroup(g) => enumerateNavigationGroup(g)
        case NetcRace(r)  => enumerateNavigationRace(r)
      }

    def enumerateNavigationGroup(group: NavigationGroup): List[NavigationEntity] = {
      val children = group.children.map(_.flatMap(enumerateNavigationGroupChild)).getOrElse(List.empty)
      if (group.isEvent)
        NavigationEventEntity(group) :: children
      else
        children
    }

    def enumerateNavigationGroupChild(groupChild: NavigationGroupChild): List[NavigationEntity] =
      groupChild match {
        case NgcGroup(g)  => enumerateNavigationGroup(g)
        case NgcMarket(m) => enumerateNavigationMarket(m)
      }

    def enumerateNavigationMarket(market: NavigationMarket): List[NavigationEntity] =
      List(NavigationMarketEntity(market))

    def enumerateNavigationRace(race: NavigationRace): List[NavigationEntity] =
      race.children.map(_.flatMap(enumerateNavigationMarket)).getOrElse(List.empty)

    def enumerateNavigationRoot(navigationRoot: NavigationRoot): List[NavigationEntity] =
      navigationRoot.children.flatMap(enumerateNavigationEventType)

    enumerateNavigationRoot(root)
  }
}
