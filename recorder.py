import os
import pygame
from levelformat import Recorder
class Recorder(pygame.sprite.Sprite):
    images = {}
    def __init__(self, recorderformat):
        pygame.sprite.Sprite.__init__(self)
        path = os.path.join('assets', 'images')
        if not Recorder.images:
            Recorder.images['idle'] = pygame.images.load(path+'recorderidle.png').convert_alpha()
            Recorder.images['play'] = pygame.images.load(path+'recorderplaying.png').convert_alpha()
            Recorder.images['record'] = pygame.images.load(path+'recorderrecording.png').convert_alpha()
            Recorder.images['saved'] = pygame.images.load(path+'recordersaved.png').convert_alpha()
        
        self.image = Recorder.images['idle']
        self.rect = self.image.get_rect
        self.initialpos = self.pos = self.rect.bottomleft
        self.actions = {}
        self.recording = None
        self.isRecording = False
        self.isSaved = False
        self.isPlaying = False
        self.isIdle = False

    def update(self, offset):
        self.pos = [a+b for a,b in zip(self.pos, offset)]
        self.rect.bottomleft = self.pos

    def startRecording(self):
        pass

    def stopRecording(self):
        pass

    def play(self):
        pass

    def reset(self):
        self.pos = self.initialpos
        self.rect.center = self.pos
