###################
##Importing Stuff##
###################
import pygame
import sys, os
import player as p
import level as l
import camera as c
from pygame.locals import *


class RedoGame():
    #############################
    ##initializing pygame stuff##
    #############################
    width = 1000
    height = 600

    def __init__(self):
        pygame.init()
        self.timer = pygame.time.Clock()
        self.screen = pygame.display.set_mode((0,0),pygame.FULLSCREEN)
        self.width, self.height = self.screen.get_size()
        ###########################
        ##initializing game stuff##
        ###########################
        path = os.path.join('assets', 'levels')
        self.levels = []
        self.states = set()
        self.switchers = []
        files = os.listdir(path)
        files.sort()
        for inFile in files:
            temp = l.Level(os.path.join(path,inFile))
            self.levels.append(temp)

        self.background = pygame.image.load(os.path.join('assets', 'images', 'background2.png')).convert()
        self.background = pygame.transform.scale(self.background, (self.width, self.height))
        self.screen.blit(self.background, [0,0])
        self.sprites = pygame.sprite.OrderedUpdates()
        self.actorsprites = pygame.sprite.Group()
        self.platformsprites = pygame.sprite.Group()
        self.buttonsprites = pygame.sprite.Group()
        self.recordersprites = pygame.sprite.Group()
        self.currentRecorder = None
        self.recording = False
        self.jump = False
        self.ticks = 0


    def updatePlatforms(self, offset):
        self.platformsprites.update(offset)
        for s in self.switchers:
            if s.group in self.states:
                s.activate()
            else:
                s.deactivate()
            if s.visible:
                self.sprites.add(s)
            else:
                self.sprites.remove(s)

        sd = pygame.sprite.groupcollide(self.actorsprites, self.platformsprites, False, False)
        for a in sd:
            for p in sd[a]:
                if p.visible:
                    if p.rect.contains(a.rect): break
                    if a.rect.bottom > p.rect.top > a.rect.top or a.rect.right > p.rect.left > a.rect.left or a.rect.left < p.rect.right < a.rect.right:
                        a.onGround = True
                    if a.rect.top < p.rect.bottom <  a.rect.bottom:
                        pass #a.onGround = False
                    while pygame.sprite.collide_rect(a,p):
                        if a.rect.bottom > p.rect.top > a.rect.top:
                            a.offset(0,-1)
                            if a.vel[1] != -100: a.vel[1] = 0
                        if a.rect.right > p.rect.left > a.rect.left and not a.rect.centery < p.rect.top:
                            a.offset(-1,0)
                            a.vel[0] = -.7*a.vel[0]
                        if a.rect.left < p.rect.right < a.rect.right and not a.rect.centery < p.rect.top:
                            a.offset(1,0)
                            a.vel[0] = -.7*a.vel[0]
                        if a.rect.top < p.rect.bottom < a.rect.bottom:
                            a.offset(0,1)
                            a.vel[1] = 0
    def updateRecorders(self, offset):
        self.recordersprites.update(offset)
        if self.recording:
            if self.currentRecorder and self.currentRecorder.isRecording:
                self.currentRecorder.record(self.jump, self.playerSprite.left, self.playerSprite.right)
            else: self.stopRecording()
            for r in self.recordersprites:
                if r.isPlaying:
                    r.play() 
        
    def updateButtons(self, offset):
        self.buttonsprites.update(offset)
        on = pygame.sprite.groupcollide(self.buttonsprites, self.actorsprites, False, False).keys()
        for button in self.buttonsprites.sprites():
            if button in on:
                button.activate()
                self.states.add(button.group)
            else:
                button.deactivate()
                if button.group in self.states:
                    self.states.remove(button.group)
            
    def update(self):
        """Updates objects in the scene."""
        #sprites.update()
        off = self.camera.update(self.playerSprite.pos)
        self.actorsprites.update(off)
        self.updateButtons(off)
        self.updateRecorders(off)
        self.exitSprite.update(off)
        self.updatePlatforms(off)

    def draw(self):
        self.sprites.clear(self.screen, self.background)
        things = self.sprites.draw(self.screen)
        #self.sprites.clear(self.screen,self.background)
        pygame.display.update(things)
        
        pygame.display.flip()
        #self.sprites.clear(self.screen,self.background)

    def levelInit(self, i):
        self.actorsprites.empty()
        self.recordersprites.empty()
        self.sprites.empty()
        self.platformsprites.empty()
        self.buttonsprites.empty()
        self.switchers = list()
        self.currentRecorder = None
        self.recording = False
        self.camera = c.Camera(self.width, self.height)
        self.exitSprite = self.levels[i].exit
        self.playerSprite = p.Player(self.levels[i].playerInitial)
        self.actorsprites.add(self.playerSprite)
        self.buttonsprites.add(self.levels[i].buttons)
        self.platformsprites.add(self.levels[i].platforms)
        self.recordersprites.add(self.levels[i].recorders)
        self.switchers = [a for a in self.platformsprites.sprites() if a.group is not None]
        self.sprites.add(self.platformsprites.sprites())
        self.sprites.add(self.exitSprite)
        self.sprites.add(self.recordersprites.sprites())
        self.sprites.add(self.playerSprite)
        self.sprites.add(self.buttonsprites.sprites())
    def resetLevel(self):
        self.recording = False
        if self.currentRecorder:
            self.currentRecorder.stopRecording()
            self.currentRecorder = None
        for p in self.platformsprites.sprites():
            p.reset()
        for b in self.buttonsprites.sprites():
            b.reset()
        for a in self.actorsprites.sprites():
            a.reset()
        self.actorsprites.empty()
        self.actorsprites.add(self.playerSprite)
        for r in self.recordersprites.sprites():
            r.reset()
        self.exitSprite.reset()


    def startPlayingStuff(self):
        for s in self.recordersprites:
            if s.isSaved and not s.isRecording:
                s.startPlaying()
                self.sprites.add(s.recording)
                self.actorsprites.add(s.recording)

    def startRecording(self):
        sp = pygame.sprite.spritecollide(self.playerSprite, self.recordersprites, False)
        self.currentRecorder = sp.pop()
        self.playerSprite.setLocation(self.currentRecorder.rect.center)
        self.currentRecorder.startRecording()
        self.startPlayingStuff()
        self.recording = True

    def stopRecording(self):
        self.recording = False
        self.playerSprite.setLocation(self.currentRecorder.rect.center)
        self.currentRecorder.stopRecording()
        self.currentRecorder = None
        for r in self.recordersprites:
            if r.recording is not None:
                r.stopPlaying()
                r.recording.remove([self.sprites, self.actorsprites])

    def mainLoop(self):
        i = 0    
        while i < len(self.levels):
            self.levelInit(i)
            while True:
                for event in pygame.event.get():
                    if event.type == pygame.QUIT:
                        sys.exit()
                    elif event.type == pygame.KEYDOWN:
                        if event.key == K_ESCAPE:
                            sys.exit()
                        elif event.key == K_w or event.key == K_UP:
                            self.playerSprite.jump()
                            self.jump = True
                        elif event.key == K_a or event.key == K_LEFT:
                            self.playerSprite.left = True
                        elif event.key == K_d or event.key == K_RIGHT:
                            self.playerSprite.right = True
                        elif event.key == K_SPACE:
                            if self.recording:
                                self.stopRecording()
                            elif pygame.sprite.spritecollideany(self.playerSprite, self.recordersprites):
                                self.startRecording()

                    elif event.type == pygame.KEYUP:
                        if event.key == K_a or event.key == K_LEFT:
                            self.playerSprite.left = False
                        elif event.key == K_d or event.key == K_RIGHT:
                            self.playerSprite.right = False
                        elif event.key == K_w or event.key == K_UP:
                            self.jump = False
                self.update()
                self.draw()
                if pygame.sprite.spritecollideany(self.exitSprite, self.actorsprites):
                    i+=1
                    break;
                elif self.playerSprite.vel[1] > 250:
                    if self.recording: self.stopRecording()
                    self.resetLevel()
                    break;
                self.timer.tick(60)

if __name__ == '__main__':
    redo = RedoGame()
    redo.mainLoop()
