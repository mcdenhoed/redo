import pygame
import sys
from actor import *
class Player(Actor):
    def __init__(self, acc):
        Actor.__init__(self, acc)
        self.image.fill((0,0,0,255))

