# 2Devs Equipment Effects #
# Work in progress ;)

### Description ###

This is a plugin that allows you to create equipment which gives you special effects like Speed, Haste etc.

### Commands ###

**TODO**

### Permissions ###

**TODO**

### Configuration ###

```yaml


effect-items:

  1:
    # id to identify item (for example in give command)
    id: "GLADIATOR_BOOTS"

    # name of item
    name: "Gladiator Boots"

    # [!] Information
    # options: must-wear | must-hold-mainhand | must-hold-offhand can be used together at the same time :)

    # if true, player have to wear that item to make it work, hands does not count(for this look at $must-hold)
    must-wear: true

    # if true, player have to hold item in main hand to make it work
    must-hold-mainhand: true

    # if true, player have to hold item in off hand to make it work
    must-hold-offhand: true

    # effects which will be applied
    # format: EFFECT:AMPLIFIER
    # EFFECTS: (https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html)
    applicable-effects:
      - "SPEED:2"

    # itemstack for which effects will work(it don't have to be given by command, just similar itemstack
    # for more information about how this section work, go here: https://www.spigotmc.org/wiki/itemstack-serialization/
    itemstack:
      ==: org.bukkit.inventory.ItemStack
      v: 2230
      type: IRON_BOOTS
      meta:
        ==: ItemMeta
        meta-type: UNSPECIFIC
        display-name: "Gladiator Boots"
```