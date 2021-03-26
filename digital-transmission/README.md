# MLT-3

## Rule

* At the beginning

1) The signal transitions from one level to the next at the beginning of a 1 bit

2) There is no transition at the beginning of a 0 bit  



* On running

1) If next bit is 0, no transition

2) If next bit is 1 and the current level is not 0, the next level is 0

3) If next bit is 1 and the current level is 0, the next level is the opposite of the last nonzero level

