# Distance Module

![keep your distance!](item:tisvs:distance_module)

The Distance Module simply outputs true or false whether it detects a solid block within a defined range of the casing.


When a target is hit, it will turn green and output 1, else it will turn red and output 0.


The distance is defined by writing to the module whenever and accepts only half-point precision floats.
The max distance allowed, by default, is 10 blocks. This can be changed via the `maxDistance` gamerule.