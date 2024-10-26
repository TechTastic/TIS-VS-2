# Framed Map

![cartographer who???](item:minecraft:item_frame)
![cartographer who???](item:minecraft:filled_map)

The serial port module is capable of pulling valuable information from maps put in Item Frames on top of the port, outputting the casing's distance from the center of the map as a half-point precision float.


To change what axis is being output, write 0 to output distance on the X-axis (default) or 1 to ouput on the Z-axis.


If the casing is outside the boundaries of a map, the module will output positive or negative infinity as half-float precision floats if the current position is positive or negative distance from the center of the map respectively.