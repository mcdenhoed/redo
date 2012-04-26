import os
import pygame
from levelformat import Recorder as r
from collections import deque
from recording import Recording
import copy
class Recorder(pygame.sprite.Sprite):
    images = {}
    def __init__(self, recorderformat):
        pygame.sprite.Sprite.__init__(self)
        path = os.path.join('assets', 'images')
        if not Recorder.images:
            Recorder.images['idle'] = pygame.image.load(os.path.join(path,'recorderidle2.png')).convert_alpha()
            Recorder.images['play'] = pygame.image.load(os.path.join(path,'recorderplaying2.png')).convert_alpha()
            Recorder.images['recording'] = pygame.image.load(os.path.join(path,'recorderrecording2.png')).convert_alpha()
            Recorder.images['saved'] = pygame.image.load(os.path.join(path,'recordersaved2.png')).convert_alpha()
        
        self.image = Recorder.images['idle']
        self.rect = self.image.get_rect()
        self.initialpos = self.pos = self.rect.midbottom = recorderformat.rect.midbottom
        self.events = deque()
        self.recording = None
        self.isRecording = False
        self.isSaved = False
        self.isPlaying = False
        self.isIdle = False
    def update(self, offset):
        self.pos = [a+b for a,b in zip(self.pos, offset)]
        self.rect.midbottom = self.pos

    def startRecording(self):
        self.isRecording = True
        self.isSaved = False
        self.isIdle = False
        self.isPlaying = False
        self.maximum = 0
        self.events = deque()
        self.image = Recorder.images['recording']
        self.backup = deque()
        self.index , self.maximum = [0,0]

    def record(self, jump, left, right):
        #Oh, no, I'm actually writing documentation!
        #This is an event list. Index zero is for jumping.
        #Index 1 is for the left key, 2 for the right
        self.events.append([jump, left, right])
        self.maximum += 1
        if self.maximum >= 900:
            self.stopRecording()
    def stopRecording(self):
        self.isRecording = False
        self.isSaved = True
        self.isPlaying = False
        self.recording = None
        self.image = Recorder.images['saved']
    
    def startPlaying(self):
        self.isRecording = False
        self.isSaved = False
        self.isPlaying = True
        self.recording = Recording(self.rect.center)
        self.recording.onGround = True
        self.image = Recorder.images['play']
        self.index = 0

    def play(self):
        if self.index < self.maximum:
            jump, left, right = self.events.popleft()
            if jump: 
                self.recording.jump()
            if left: self.recording.leftPress()
            elif right: self.recording.rightPress()
            self.events.append([jump,left,right])
            self.index += 1
        else:
            self.stopPlaying()
        
    def stopPlaying(self):
        self.isRecording = False
        self.isSaved = True
        self.isPlaying = False
        self.image = Recorder.images['saved']
        for i in range(self.index, self.maximum):
            self.events.append(self.events.popleft())        

    def reset(self):
        self.pos = self.initialpos
        self.rect.midbottom = self.pos
