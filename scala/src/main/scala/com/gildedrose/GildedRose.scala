package com.gildedrose

class GildedRose(val items: Array[Item]) {
  private def decreaseQuality(item: Item, amount: Int): Unit = {
    item.quality = Math.max(Item.minQuality, item.quality - amount)
  }

  private def increaseQuality(item: Item, amount: Int): Unit = {
    item.quality = Math.min(Item.maxQuality, item.quality + amount)
  }

  def updateQuality() {
    items.filter(_.name != Item.sulfuras).foreach { item =>
      item.sellIn -= 1

      val conjured = item.name.toLowerCase.trim.startsWith("conjured")

      val sellInFactor = if (item.sellIn < 0) 2 else 1
      item.name match {
        case Item.backstagePass if item.sellIn < 0 =>
          item.quality = Item.minQuality
        case Item.backstagePass if item.sellIn < 5 =>
          increaseQuality(item, 3)
        case Item.backstagePass if item.sellIn < 10 =>
          increaseQuality(item, 2)
        case Item.backstagePass =>
          increaseQuality(item, 1)
        case Item.agedBrie =>
          increaseQuality(item, 1 * sellInFactor)
        case _ if conjured =>
          decreaseQuality(item, 2 * 1 * sellInFactor)
        case _ =>
          decreaseQuality(item, 1 * sellInFactor)
      }
    }
  }
}
