import pygame
import sys
from actor import *
class Player(Actor):
    def __init__(self, (xa, ya)):
        print "new player"
        Actor.__init__(self, (xa,ya))

