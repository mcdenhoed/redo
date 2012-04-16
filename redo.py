#Hello! This is my game.
import pygame
import sys
from player import * 
from pygame.locals import *
width = 1000
height = 600
pygame.init()
timer = pygame.time.Clock()
#mouse = 0,0 #Needed for menus?
screen = pygame.display.set_mode((width, height))
player = Player(100.0,100.0)
actors = pygame.sprite.RenderClear((player))
def update():
    """Updates objects in the scene."""
    actors.update()

def draw():
    screen.fill((200,200,200))
    actors.draw(screen)
    pygame.display.flip()

while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            #pygame.quit()
            sys.exit()
        elif event.type == pygame.KEYDOWN:
            if event.key == K_w or event.key == K_UP:
                pass
            elif event.key == K_a or event.key == K_LEFT:
                player.left = True
            elif event.key == K_d or event.key == K_RIGHT:
                player.right = True
            
    update()
    draw()
    timer.tick(60)

