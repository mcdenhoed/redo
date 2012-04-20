###################
##Importing Stuff##
###################
import pygame
import sys, os
import player as p
import level as l
from pygame.locals import *

#############################
##initializing pygame stuff##
#############################
width = 1000
height = 600
pygame.init()
timer = pygame.time.Clock()
screen = pygame.display.set_mode((width, height))
###########################
##initializing game stuff##
###########################
path = os.path.join('assets', 'levels')
levels = []
for inFile in os.listdir(path):
    temp = l.Level(os.path.join(path,inFile))
    levels.append(temp)

background = pygame.Surface([width,height])
background.fill([200,200,200])
screen.blit(background, [0,0])
sprites = pygame.sprite.RenderUpdates()
actorsprites = pygame.sprite.Group()
platformsprites = pygame.sprite.Group()
buttonsprites = pygame.sprite.Group()

def update():
    """Updates objects in the scene."""
   #sprites.update()
    actorsprites.update()
    buttonsprites.update()
    platformsprites.update([0,0])
    sd = pygame.sprite.groupcollide(actorsprites, platformsprites, False, False)
    for a in sd:
        for p in sd[a]:
            if a.rect.bottom > p.rect.top > a.rect.top:
                a.onGround = True
            while a.rect.bottom > p.rect.top > a.rect.top:
                a.offset(0,-1)          
def draw():
    things = sprites.draw(screen)
    pygame.display.update(things)
    sprites.clear(screen,background)
i = 0    
while i < len(levels):
    actorsprites.empty()
    sprites.empty()
    platformsprites.empty()
    buttonsprites.empty()
    exitSprite = levels[i].exit
    playerSprite = p.Player(levels[i].playerInitial)
    actorsprites.add(playerSprite)
    buttonsprites.add(levels[i].buttons) 
    platformsprites.add(levels[i].platforms)
    sprites.add(playerSprite)
    sprites.add(levels[i].buttons)
    sprites.add(levels[i].platforms)
    sprites.add(exitSprite)
    while True:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                #pygame.quit()
                sys.exit()
            elif event.type == pygame.KEYDOWN:
                if event.key == K_w or event.key == K_UP:
                    playerSprite.jump()
                elif event.key == K_a or event.key == K_LEFT:
                    playerSprite.left = True
                elif event.key == K_d or event.key == K_RIGHT:
                    playerSprite.right = True
            elif event.type == pygame.KEYUP:
                if event.key == K_a or event.key == K_LEFT:
                    playerSprite.left = False
                elif event.key == K_d or event.key == K_RIGHT:
                    playerSprite.right = False 
        update()
        draw()
        if pygame.sprite.spritecollideany(exitSprite, actorsprites):
            i+=1
            print "level complete"
            break;
        elif playerSprite.vel[1] > 250:
            print "You dead"
            break;
        timer.tick(60)
        

