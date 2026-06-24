# Super Chest (Fabric, Minecraft 1.21.8) — Stage 1

A new **Super Chest** block:
- Holds **216 items** (9 x 24) = **4x a double chest**.
- Name shows as **Super Chest in yellow**.
- **Scrollable** inventory screen (mouse wheel scrolls through all 24 rows).
- Crafting recipe: iron ingot in the 4 corners, oak log on the 4 edges, a
  chest in the middle.

Stage 2 (the double-chest 8x version) comes after this one works in-game.

## Build it (same GitHub method as your other mods)
1. New repository.
2. First create `.github/workflows/build.yml` via **Add file -> Create new
   file** and paste the workflow (also in this zip). Commit.
3. **Add file -> Upload files**: drag in everything else (`build.gradle`,
   `gradle.properties`, `settings.gradle`, `src`, `gradle`). Commit.
4. **Actions** tab -> wait for green -> download **superchest-mod** ->
   use `superchest-1.0.0.jar` (not `-sources`).

## How to get one in-game
Craft it with the recipe above, or run `/give @s superchest:super_chest`.
(There's no creative-tab entry yet — that needs an extra library; crafting and
/give both work fine.)

## This is a big mod built without testing — expect a fix round or two
If the build goes **red**, open the failed run, find the **first** red error,
and send it to me. The most likely spots (and they're all isolated, so a fix is
quick) are:

1. **Item dropping on break** — `SuperChestBlock.onStateReplaced` and the
   `ItemScatterer` import. If this is the error, you can temporarily delete that
   whole `onStateReplaced` method to get a working build (you'd just lose items
   if you break the chest), then I'll fix it properly.
2. **The scrolling screen** — `SuperChestScreen` uses `slot.x` / `slot.y` and
   the draw methods; method names here occasionally shift between versions.
3. **Registration** — `registryKey(...)` / `useBlockPrefixedTranslationKey()`
   in `SuperChestMod`.
4. **Recipe / loot table JSON** — these use the 1.21.8 format (`"id"`,
   `recipe/`, `loot_table/`).

Send me the error text and I'll point at the exact line.
