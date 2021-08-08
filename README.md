# Infinite Obsidian Plugin!
This plugin allows you to place obsidian without the stack running out. It will take money out of your account every time you place a block!
This is a PLUGIN, and it goes into your plugins folder on your server. It will not work if its anywhere else!

## How it works:
You place the Infinite Obsidian block, and it refills the stack to 64, while taking a configurable amount of money out of your balance (VAULT), and then allows you to keep placing over and over.

### Commands:
*Values in **[]** are required, values in **<>** are optional*
 - ```/infiniteobsidian give [Player] <Amount>```   - ``Gives a player an Infinite Obsidian Block.``


### Permissions:
 - ```infiniteobsidian.give```     - ``Gives permissions to use /infiniteobsidian give.``
 
### Config.yml, with comments explaining everything.
```yaml
item:
  displayName: "&c&lInfinite Obsidian"
  lore:
    - "&f"
    - "&7Place this block to never run out of obsidian"
    - "&7It will charge &a${price} &7each time its placed"
  type: BEDROCK
  glow: true
pricePer: 100
block: OBSIDIAN
messageEveryXSeconds: 45
message:
  cantAfford: "&cYou do not have a sufficient balance to place an Infinite Obsidian block"
  charged: "&7You we're charged &a${price} &7for your recent Infinite Obsidian placements in the last {sec} seconds."
  noPermission: "&cYou do not have permission to give a gen bucket."
  usage: "&cUsage: &f/InfiniteObsidian give <user> [amount]"
  cantFind: "&cCould not find target"
  invalidNumber: "&cInvalid number"
  youGave: "&7You gave &c{player} &fx{amt} &7Infinite Obsidian Blocks."
```

## Need help? 
Join the [Support Server](https://discord.i0dev.com/) and feel free to ask any questions to me directly too, My discord is i0#0001
