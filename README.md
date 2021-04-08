# 2Devs Equipment Effects #

# Work in progress ;)

### Description ###

This is a plugin that allows you to create equipment or held items which gives you potion effects, permissions,
particles or execute commands :)

### Todo List ###

- Giving player permission on effect
- Particles
- Executing commands
- NBT support
- WorldGuard Support
- Support for projectiles
- Command to create items in-game
- Auto completion

Note: Thanks to https://github.com/kawaii for all suggestions <3

### Commands ###

- /EquipmentEffectsAdmin reload - To reload Configuration
- /EquipmentEffectsAdmin list - To check list of created items
- /EquipmentEffectsAdmin give {PLAYER} {ITEM_ID} - Give Item to player
- /EquipmentEffectsAdmin give {ITEM_ID} - Give Item

### Permissions ###

- /EquipmentEffectsAdmin - EquipmentEffects.admin

### Item Configuration ###

```yaml
# that's  default itemstack, if you want to create another,
# just create new file with own name, you need .yml as extension ;)


# id to identify item (for example in give command)
id: "DEFAULT"

# name of item
name: "Default item"

# [!] Information
# options: must-wear | must-hold-mainhand | must-hold-offhand can be used together at the same time :)

# if true, player have to wear that item to make it work, hands does not count
must-wear: true

# if both {must-hold-mainhand} and {must-hold-offhand} that makes it work for both hands

# if true, player have to hold item in main hand to make it work
must-hold-mainhand: false

# if true, player have to hold item in off hand to make it work
must-hold-offhand: false

# effects which will be applied
# format: EFFECT:AMPLIFIER
# EFFECTS: (https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html)
applicable-effects:
  - "SPEED:2"

# section for interactions on triggering with {must-wear} or {must-hold-mainhand} or {must-hold-offhand}
trigger-interactions:

  # trigger on triggering enable for interaction ( example; holding item )
  trigger-enable:

    # command which will be executed
    command:
      - "say Trigger on"
      - "say Test123"

  # trigger on triggering disable for interaction ( example; un-holding item )
  trigger-disable:

    # command which will be executed
    command:
      - "say Trigger off"
      - "say Test123"


# itemstack for which effects will work(it don't have to be given by command, just similar itemstack
# for more information about how this section work, go here: https://www.spigotmc.org/wiki/itemstack-serialization/
itemstack:
  ==: org.bukkit.inventory.ItemStack
  v: 2230
  type: IRON_BOOTS
  meta:
    ==: ItemMeta
    meta-type: UNSPECIFIC
    display-name: "Default Item"
```

### Configuration ###

```yaml
# this is version of config, if it is different from plugin version that means that you have outdated config!
config-version: ${project.version}

# partition minimum players amount multiplier
# that's multiplier of {update-partitions-amount}, if you have this value set to 4, and you use
# default 2 multiplier, it will enable partitioning when you have at least 8 players, below this number
# it will update for all players at once
#
# 2 is minimum, otherwise it can throw an IllegalArgumentException
partition-minimum-players-multiplier: 2

# to how many partitions split player count
# ex: 105 players on server, if you specify {update-partitions-amount} as 4 it will split player inventory calculation
# for 4 parts; 25, 25, 25, 30, every part will be calculated every {task-schedule-time}, with default value of 20,
# it will take server 4 seconds to calculate all players inventory, and it will constantly repeat
update-partitions-amount: 4

# update player inventory(apply effects) task period, in ticks(20 ticks = 1 sec)
task-schedule-time: 20

messages:
  admin-command-usage: "Usage of admin command: /EEA give <id> | /EEA list | /EEA reload"
  reload: "Config got reloaded!"
  no-permissions: "No Permissions!"
```
