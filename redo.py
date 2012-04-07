#Hello! This is my game.
import pygame
import sys

width = 1000
height = 600
pygame.init()
timer = pygame.time.Clock()
mouse = 0,0
screen = pygame.display.set_mode((width, height))
player = pygame.image.load("assets/images/exit.png").convert_alpha()
playRect = player.get_rect()

def update():
    """Updates objects in the scene."""
    global playRect
    playRect = playRect.move((0.05*(mouse[0]-playRect.left), 0.05*(mouse[1]-playRect.top)))

def draw():
    global screen
    global player
    global playRect
    """Draws objects onto the screen."""
    screen.fill((0,0,0))
    screen.blit(player, playRect)

#if __name__ == "__main__":

while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            #pygame.quit()
            sys.exit()
        elif event.type == pygame.MOUSEMOTION:
            mouse = event.pos
    #playRect = playRect.move((mouse[0]-playRect.left, mouse[1]-playRect.top))
    update()
    draw()
    #screen.fill((0,0,0))
    #screen.blit(player, playRect)
    pygame.display.flip()
    timer.tick(65)

