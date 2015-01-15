# bigbrother
Smart Dust Simulator

## Research Goals
Something something GPS data.

### What
Smart dust is essentially a tiny micro electrical sensor that can detect anything from light to vibrations. They are essentially a multifunctional mote.

### Where
Smart dust is small, and versatile enough that it can be used in numerous areas. It can be used in things like espionage, geological surveys, hospitals, etc.

### Who
Depending on its intended use, it could be used by the military to gather intelligence, scientists to gather data on geological structures, and by IT in buildings to control/monitor temperatures.
How does the intended use of this technology change, depending on who's using it? 

### Why
Good uses: Could be used in many different settings as stated above. With devices so small, who has access to the information that they are providing? What is the information being used for? How can privacy be affected by a device as small as this? How does our simulation demonstrate just how valuable simple metadata can be? Also, keep in mind that the research behind this technology has been funded by the Defense Advanced Research Projects Agency since 1998, so what does this say about some of the potential market?

Remember, these devices could be 1mm in size, and cost as little as one dollar (currently they cost 50-100$, and are 5mm).

### When
These devices are available now. They will only get cheaper and smaller as time goes on.

## Proof of Concept

### Stage I
We mine the GPS data from people's phones, and map the information to essentially show the makeup of the interior of a building. Realistically, just mining or even mapping the data through the city may be enough to show proof of concept, as it is a bit of an invasion of privacy. I'm not sure how accurate our phone’s GPSs are when it comes to vertical distances, so we may be limited to one floor per building.

### Stage II a
We can find out whether a person is walking, or in a car depending on how far they travel over a certain time interval. This may add challenges - it may be difficult to distinguish whether someone is in a building, or if you're outside unless it's mapped onto something like google maps. 

### Stage II b
Using the microphone we could keep track of the dB of the sound in an area to estimate how many people were in the area at the time of the data being collected (essentially profiling). I’m not quite sure on the complexity of this, as I think it may highly depend on what kind of libraries are available in the OS that we choose.

## Sources:
http://www.computerworld.com/article/2581821/mobile-wireless/smart-dust.html
