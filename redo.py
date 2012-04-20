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
        self.screen = pygame.display.set_mode((RedoGame.width, RedoGame.height))
        ###########################
        ##initializing game stuff##
        ###########################
        path = os.path.join('assets', 'levels')
        self.levels = []
        self.states = set()
        self.switchers = []
        for inFile in os.listdir(path):
            temp = l.Level(os.path.join(path,inFile))
            self.levels.append(temp)

        self.background = pygame.Surface([RedoGame.width,RedoGame.height])
        self.background.fill([200,200,200])
        self.screen.blit(self.background, [0,0])
        self.sprites = pygame.sprite.RenderUpdates()
        self.actorsprites = pygame.sprite.Group()
        self.platformsprites = pygame.sprite.Group()
        self.buttonsprites = pygame.sprite.Group()

    def updatePlatforms(self, offset):
        self.platformsprites.update(offset)
        for s in self.switchers:
            if s.group in self.states:
                s.visible = True
                self.sprites.add(s)
            else:
                s.visible = False
                if s in self.sprites.sprites():
                    self.sprites.remove(s)

        sd = pygame.sprite.groupcollide(self.actorsprites, self.platformsprites, False, False)
        for a in sd:
            for p in sd[a]:
                if a.rect.bottom > p.rect.top > a.rect.top:
                    a.onGround = True
                while a.rect.bottom > p.rect.top > a.rect.top:
                    a.offset(0,-1)
        
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
        off = self.camera.update(self.playerSprite.rect.center)
        self.actorsprites.update(off)
        self.updateButtons(off)
        self.updatePlatforms(off)
        self.exitSprite.update(off)

    def draw(self):
        self.sprites.clear(self.screen, self.background)
        things = self.sprites.draw(self.screen)
        #self.sprites.clear(self.screen,self.background)
        pygame.display.update(things)
        
        pygame.display.flip()
        #self.sprites.clear(self.screen,self.background)

    def levelInit(self, i):
        self.actorsprites.empty()
        self.sprites.empty()
        self.platformsprites.empty()
        self.buttonsprites.empty()
        self.camera = c.Camera(self.width, self.height)
        self.exitSprite = self.levels[i].exit
        self.playerSprite = p.Player(self.levels[i].playerInitial)
        self.actorsprites.add(self.playerSprite)
        self.buttonsprites.add(self.levels[i].buttons)
        self.platformsprites.add(self.levels[i].platforms)
        self.switchers = [a for a in self.platformsprites.sprites() if a.group is not None]
        self.sprites.add(self.playerSprite)
        self.sprites.add(self.levels[i].buttons)
        self.sprites.add([a for a in self.levels[i].platforms if a.visible is True])
        self.sprites.add(self.exitSprite)


    def mainLoop(self):
        i = 0    
        while i < len(self.levels):
            self.levelInit(i)
            while True:
                for event in pygame.event.get():
                    if event.type == pygame.QUIT:
                        #pygame.quit()
                        sys.exit()
                    elif event.type == pygame.KEYDOWN:
                        if event.key == K_w or event.key == K_UP:
                            self.playerSprite.jump()
                        elif event.key == K_a or event.key == K_LEFT:
                            self.playerSprite.left = True
                        elif event.key == K_d or event.key == K_RIGHT:
                            self.playerSprite.right = True
                    elif event.type == pygame.KEYUP:
                        if event.key == K_a or event.key == K_LEFT:
                            self.playerSprite.left = False
                        elif event.key == K_d or event.key == K_RIGHT:
                            self.playerSprite.right = False 
                self.update()
                self.draw()
                if pygame.sprite.spritecollideany(self.exitSprite, self.actorsprites):
                    i+=1
                    print "level complete"
                    break;
                elif self.playerSprite.vel[1] > 250:
                    print "You dead"
                    break;
                self.timer.tick(60)

if __name__ == '__main__':
    redo = RedoGame()
    redo.mainLoop()
