import pygame
from collections import dequeue
class Recording(Actor):
    def __init__(self, (x,y)):
        print "new recording"
        Actor.__init__(self, (x,y))
        
