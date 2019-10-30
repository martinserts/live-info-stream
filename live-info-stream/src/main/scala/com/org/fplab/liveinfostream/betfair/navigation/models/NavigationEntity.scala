package com.org.fplab.liveinfostream.betfair.navigation.models

sealed trait NavigationEntity

final case class NavigationEventEntity(event: NavigationGroup) extends NavigationEntity
final case class NavigationMarketEntity(market: NavigationMarket) extends NavigationEntity

object NavigationEntity {
  def enumerate(root: NavigationRoot): List[NavigationEntity] = {
    def EnumerateNavigationEventType(eventType: NavigationEventType): List[NavigationEntity] =
      eventType.children.map(_.flatMap(EnumerateNavigationEventTypeChild)).getOrElse(List.empty)

    def EnumerateNavigationEventTypeChild(eventTypeChild: NavigationEventTypeChild): List[NavigationEntity] = eventTypeChild match {
      case NetcGroup(g) => EnumerateNavigationGroup(g)
      case NetcRace(r) => EnumerateNavigationRace(r)
    }

    def EnumerateNavigationGroup(group: NavigationGroup): List[NavigationEntity] = {
      val children = group.children.map(_.flatMap(EnumerateNavigationGroupChild)).getOrElse(List.empty)
      if (group.isEvent)
        NavigationEventEntity(group) :: children
      else
        children
    }

    def EnumerateNavigationGroupChild(groupChild: NavigationGroupChild): List[NavigationEntity] = groupChild match {
      case NgcGroup(g) => EnumerateNavigationGroup(g)
      case NgcMarket(m) => EnumerateNavigationMarket(m)
    }

    def EnumerateNavigationMarket(market: NavigationMarket): List[NavigationEntity] = List(NavigationMarketEntity(market))

    def EnumerateNavigationRace(race: NavigationRace): List[NavigationEntity] =
      race.children.map(_.flatMap(EnumerateNavigationMarket)).getOrElse(List.empty)

    def EnumerateNavigationRoot(root: NavigationRoot): List[NavigationEntity] =
      root.children.flatMap(EnumerateNavigationEventType)

    EnumerateNavigationRoot(root)
  }
}