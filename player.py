import pygame
import sys
from actor import *
class Player(Actor):
    def __init__(self, (xa, ya)):
        Actor.__init__(self, (xa,ya))

