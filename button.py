from leveleditor import Button as bf
import pygame
import os
class Button(pygame.sprite.Sprite):
    images = {}
    def __init__(self, buttonFormat):
        pygame.sprite.Sprite.__init__(self)
        if not Button.images:
            imgpath = os.path.join("assets", "images")
            Button.images['inactive'] = pygame.image.load(imgpath + "buttonInact.png").convert_alpha()
            Button.images['active'] = pygame.image.load(imgpath + "buttonAct.png").convert_alpha()
        self.image = Button.images['inactive']
        self.rect = self.image.get_rect()
        self.activated = False
        self.group = buttonFormat.sets
        self.rect.center = buttonFormat.rect.center 
