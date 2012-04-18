import sys, os, pygame
import cPickle as pickle
import platform
import exit
import recorder
import button
from leveleditor import *
print(LevelFormat)
class Level:
    def __init__(self, levelFile):
        levelFormat = LevelFormat(None,None,None,None,None)
        levelFormat = pickle.load(open(levelFile,'rb'))
        self.playerInitial = levelFormat.gamePlayer.rect.center
        #self.recorders = [recorder.Recorder(r) for r in levelFormat.recorders]
        self.platforms = [platform.Platform(p) for p in levelFormat.platforms]
        self.exit = exit.Exit(levelFormat.exit)
        self.buttons = [button.Button(b) for b in levelFormat.buttons]
