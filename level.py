import sys, os, pygame
import leveleditor as le
import cpickle as pickle
def class Level:
    def __init__(self, levelFile):
        levelformat = pickle.load(open(levelFile, 'rb'))
        self.playerInitial = levelFormat.gamePlayer.rect.center
        self.recorders = [r.rect for r in levelFormat.recorders]
        #Ok, everything gets its own class.
