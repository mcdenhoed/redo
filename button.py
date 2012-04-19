from levelformat import Button as bf
import pygame
import os
class Button(pygame.sprite.Sprite):
    images = {}
    def __init__(self, buttonFormat):
        pygame.sprite.Sprite.__init__(self)
        if not Button.images:
            inactiveImgPath = os.path.join("assets", "images", "buttonInactive.png")
            activeImgPath = os.path.join("assets", "images", "buttonActive.png")
            Button.images['inactive'] = pygame.image.load(inactiveImgPath).convert_alpha()
            Button.images['active'] = pygame.image.load(activeImgPath).convert_alpha()
        self.image = Button.images['inactive']
        self.rect = self.image.get_rect()
        self.activated = False
        self.group = buttonFormat.sets
        self.rect.bottomleft = buttonFormat.rect.bottomleft 
