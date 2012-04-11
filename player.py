import pygame
import sys
from actor import *
class Player(Actor):
    def __init__(self, xa=0.0, ya=0.0):
        Actor.__init__(self, x=xa,y=ya)

