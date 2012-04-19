import pygame

RED = (255,0,0)
BLUE = (0,0,255)
GREEN = (0,255,0)
ORANGE = (255,255,0)
PURPLE = (255,0,255)
AQUA = (0,255,255)
BLACK = (0,0,0)
WHITE = (255,255,255)
LAST = [0,0]
CURRENT = [0,0]
class LevelFormat:
    def __init__(self, player, recorders, buttons, platforms, exit):
        self.player = player
        self.recorders = recorders
        self.buttons = buttons
        self.platforms = platforms
        self.exit = exit
class Player:
    def __init__(self, x,y, w, h):
        self.color = BLUE
        self.rect = pygame.Rect(x,y,w,h)

    def draw(self, surface):
        pygame.draw.rect(surface, self.color, self.rect)

class Recorder:
    def __init__(self,x,y):
        self.color = ORANGE
        self.rect = pygame.Rect(x,y,100,100)

    def draw(self, surface):
        pygame.draw.rect(surface, self.color, self.rect)

class Button:
    def __init__(self,x,y):
        self.color = PURPLE
        self.rect = pygame.Rect(x,y,70,70)
        self.sets = None
    def draw(self, surface):
        pygame.draw.rect(surface, self.color, self.rect)

class Exit:
    def __init__(self,x,y):
        self.color = RED
        self.rect = pygame.Rect(x,y,100,100)
        #self.setBy = None
    def draw(self, surface):
        pygame.draw.rect(surface, self.color, self.rect)

class Platform:
    def __init__(self, x, y, w, h):
        self.color = BLACK
        self.rect = pygame.Rect(x,y,w,h)
        self.rect.normalize()
        self.setBy = None
        self.visibleDefault = True
    def draw(self, surface):
        pygame.draw.rect(surface, self.color, self.rect)

