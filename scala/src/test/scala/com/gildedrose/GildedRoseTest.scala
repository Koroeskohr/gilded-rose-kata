package com.gildedrose

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalactic.{CanEqual, Equivalence}

class GildedRoseTest extends AnyFunSpec with Matchers {
  describe("the items") {
    it("should still contain the same number of items after updating quality") {
      val items = Array[Item](
        new Item("foo", 0, 0),
        new Item("bar", 0, 0)
      )
      val app = new GildedRose(items)
      app.updateQuality()
      app.items.size should equal(2)
    }
  }

  describe("an item") {
    it("should have a positive quality") {
      val items = Array[Item](
        new Item(Item.agedBrie, 0, 0),
        new Item(Item.sulfuras, 0, 80),
        new Item(Item.backstagePass, 0, 50),
        new Item(Item.agedBrie, 0, 50)
      )
      val app = new GildedRose(items)
      for (i <- 0 to 1000) {
        app.updateQuality()
        app.items.map(_.quality).foreach { _ should be >= 0 }
      }
    }
  }

  describe("specific items categories") {
    describe("Sulfuras") {
      it("should always have a quality of 80") {
        val items = Array[Item](
          new Item(Item.sulfuras, 10, 80)
        )
        val app = new GildedRose(items)

        for (i <- 0 to 1000) {
          app.updateQuality()
          app.items(0).quality should equal(80)
        }
      }

      it("doesn't change sellIn") {
        val items = Array[Item](
          new Item(Item.sulfuras, 10, 80)
        )
        val app = new GildedRose(items)

        for (i <- 0 to 1000) {
          app.updateQuality()
          app.items(0).sellIn should equal(10)
        }
      }
    }

    describe("backstage passes") {
      they("should drop to quality 0 after sellIn is passed") {
        val items = Array[Item](
          new Item(Item.backstagePass, 0, 10000)
        )
        val app = new GildedRose(items)

        app.items(0).quality should equal(10000)
        app.updateQuality()
        app.items(0).quality should equal(0)
      }

      they("should increase in quality everyday until sellIn") {
        val items = Array[Item](
          new Item(Item.backstagePass, 10, 10)
        )
        val app = new GildedRose(items)

        for (i <- 0 to 1000) {
          val currentPassQuality = app.items(0).quality
          app.updateQuality()
          if (app.items(0).sellIn < 0) {
            app.items(0).quality should equal(0)
          } else {
            // handle max quality with >=
            app.items(0).quality should be >= currentPassQuality
          }
        }
      }
    }

    describe("aged brie") {
      it("should increase in quality everyday") {
        val items = Array[Item](
          new Item(Item.agedBrie, 10, 10)
        )
        val app = new GildedRose(items)

        for (i <- 0 to 1000) {
          val currentBrieQuality = app.items(0).quality
          app.updateQuality()
          if (currentBrieQuality == Item.maxQuality) {
            app.items(0).quality should equal(currentBrieQuality)
          } else {
            app.items(0).quality should be > currentBrieQuality
          }
        }
      }

      it("should have a non zero quality after sellIn is zero") {
        val items = Array[Item](
          new Item(Item.agedBrie, 10, 10)
        )
        val app = new GildedRose(items)

        for (i <- 0 to 1000) {
          app.updateQuality()
          app.items(0).quality should be > 0
        }
      }
    }
  }

  describe("real-data test cases") {
    it("should work") {
      val items = Array[Item](
        new Item("+5 Dexterity Vest", 10, 20),
        new Item("Aged Brie", 2, 0),
        new Item("Elixir of the Mongoose", 5, 7),
        new Item("Sulfuras, Hand of Ragnaros", 0, 80),
        new Item("Sulfuras, Hand of Ragnaros", -1, 80),
        new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
        new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),
        new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),
        new Item("Conjured Mana Cake", 3, 6)
      )
      val app = new GildedRose(items)
      app.updateQuality()

      app.items(0).sellIn should equal(9)
      app.items(0).quality should equal(19)
      app.items(1).sellIn should equal(1)
      app.items(1).quality should equal(1)
      app.items(2).sellIn should equal(4)
      app.items(2).quality should equal(6)
      app.items(3).sellIn should equal(0)
      app.items(3).quality should equal(80)
      app.items(4).sellIn should equal(-1)
      app.items(4).quality should equal(80)
      app.items(5).sellIn should equal(14)
      app.items(5).quality should equal(21)
      app.items(6).sellIn should equal(9)
      app.items(6).quality should equal(50)
      app.items(7).sellIn should equal(4)
      app.items(7).quality should equal(50)
      app.items(8).sellIn should equal(2)
      app.items(8).quality should equal(4)
    }
  }
}
