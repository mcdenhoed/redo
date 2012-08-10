import sys, os, pygame
import pickle
import platform
import exit
import recorder
import button
import levelformat
class Level:
    def __init__(self, levelFile):
        levelFormat = pickle.load(open(levelFile,'rb'))
        self.playerInitial = levelFormat.player.rect.center
        #self.recorders = [recorder.Recorder(r) for r in levelFormat.recorders]
        self.platforms = [platform.Platform(p) for p in levelFormat.platforms]
        self.exit = exit.Exit(levelFormat.exit)
        self.recorders = [recorder.Recorder(r) for r in levelFormat.recorders]
        self.buttons = [button.Button(b) for b in levelFormat.buttons]
        self.done = False
