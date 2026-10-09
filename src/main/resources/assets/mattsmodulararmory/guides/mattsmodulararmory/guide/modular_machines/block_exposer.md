---
navigation:
  title: Block Exposer
  position: 1
  icon: exposer
  parent: modular_machines/modular_machines.md

item_ids:
  - exposer
---

# Block Exposer

Transforms or creates items depending on its surroundings

<Recipe id="exposer" />

When the block is adjacent on any of the 6 sides to correct blocks or fluids it can transform some items into others

The block can also generate items when the slot is filled with the <ItemLink id="exposer_slot_plug" />

<GameScene zoom="3">
  <ImportStructure src="../structures/exposer.snbt" />
  <RemoveBlocks id="minecraft:glass" />
  <RemoveBlocks id="minecraft:glass" />
</GameScene>
Exposer generating cobblestone in all configurations