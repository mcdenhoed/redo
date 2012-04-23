import pygame
import cPickle as pickle
import sys, os
from pygame.locals import *
import easygui as eg
import levelformat 

class MyButton:
    """Button class based on the
    template method pattern."""
    
    def __init__(self, x, y, w, h):
        self.rect = Rect(x, y, w, h)
        self.msg = ""
    def containsPoint(self, x, y):
        return self.rect.collidepoint(x, y)
    def draw(self, surface):
        # You could of course use pictures here.
        # This method could also be implemented
        # by subclasses.
        pygame.draw.rect(
            surface,
            (150,150,150), #gray
            self.rect,
            )
        # Here's some stuff
        font = pygame.font.Font(None, 24)
        text = font.render(self.msg, 1, (10,10,10))
        textpos = text.get_rect(center=self.rect.center)
        surface.blit(text, textpos)
    def do(self):
        print "Impbement in subclasses"

class RectButton(MyButton):
    def __init__(self, x, y, w, h, app):
        # This is how you call the superclass init
        MyButton.__init__(self, x, y, w, h)
        self.app = app
        self.msg = "Platform"
    def do(self):
        SimpleUI.state['mode'] = 'rect'
        
class PlayerButton(MyButton):
    def __init__(self, x, y, w, h, app):
        MyButton.__init__(self, x, y, w, h)
        self.app = app
        self.msg = "Player"
    def do(self):
        SimpleUI.state['mode'] = 'player'

class ExitButton(MyButton):
    def __init__(self, x, y, w, h, app):
        MyButton.__init__(self, x, y, w, h)
        self.app = app
        self.msg = "Exit"
    def do(self):
        SimpleUI.state['mode'] = 'exit'

class ButtonButton(MyButton):
    def __init__(self, x, y, w, h, app):
        MyButton.__init__(self, x, y, w, h)
        self.app = app
        self.msg = "Button"
    def do(self):
        SimpleUI.state['mode'] = 'button'

class RecorderButton(MyButton):
    def __init__(self, x, y, w, h, app):
        MyButton.__init__(self, x, y, w, h)
        self.app = app
        self.msg = "Recorder"
    def do(self):
        SimpleUI.state['mode'] = 'recorder'

class SaveButton(MyButton):
    def __init__(self, x, y, w, h, app):
        MyButton.__init__(self, x, y, w, h)
        self.filename = 'default'
        self.msg = 'Save'
        self.app = app
    def do(self):
        level = levelformat.LevelFormat(self.app.gamePlayer, self.app.gameRecorders, self.app.gameButtons, self.app.gameRects, self.app.gameExit)
        self.filename = eg.enterbox(msg='Enter a filename for saving.', title='Saving Stuff', default=self.filename)
        if self.filename is not None:
            pickle.dump(level, open(os.path.join('assets', 'levels', self.filename + '.p'), 'wb'))
        
class LoadButton(MyButton):
    def __init__(self, x, y, w, h, app):
        MyButton.__init__(self, x, y, w, h)
        self.app = app
        self.msg = "Load"
        self.filename = None
    def do(self):
        self.filename = eg.enterbox(msg='Enter a file to load.', title='Loading Stuff')
        if self.filename is not None:
            level = pickle.load(open(os.path.join('assets', 'levels', self.filename + '.p'), 'rb'))
            self.app.gamePlayer, self.app.gameRecorders, self.app.gameButtons, self.app.gameRects, self.app.gameExit = level.player, level.recorders, level.buttons, level.platforms, level.exit

class SimpleUI:
    state = {'mode' : 'none', 'clicked' : False}
    LAST = [0,0]
    CURRENT = [0,0]
    WHITE = (255,255,255)
    BLUE = (0,0,255)
    GREEN = (0,255,0)
    RED = (255,0,0)
    def __init__(self, width=1000, height=500):
        # Initialize PyGame
        pygame.init()
        pygame.display.set_caption("Simple UI")
        self.selected = None
        self.screen = pygame.display.set_mode((width,height))
        self.screen.fill(SimpleUI.WHITE)
        self.buttons = []
        self.addButton(RectButton(20, 20, 100, 50, self))
        self.addButton(PlayerButton(20, 90, 100, 50, self))
        self.addButton(ExitButton(20, 160, 100, 50, self))
        self.addButton(ButtonButton(20, 230, 100, 50, self))
        self.addButton(RecorderButton(20, 300, 100, 50, self))
        self.addButton(LoadButton(20, 370, 100, 50, self))
        self.addButton(SaveButton(20, 440, 100, 50, self))
        self.gameButtons = []
        self.gamePlayer = levelformat.Player(200, 200, 30, 30)
        self.gameRects = []
        self.gameRecorders = []
        self.gameExit = levelformat.Exit(400, 200)
    def addButton(self, button):
        self.buttons = self.buttons + [button]
        
    def run(self):
        # Run the event loop
        self.loop()
        # Close the Pygame window
        pygame.quit()
    
    def loop(self):
        clock = pygame.time.Clock()
        exit = False
        while not exit:
            exit = self.handleEvents()
            self.draw()
            clock.tick(30) 

    def handleEvents(self):
            exit = False
            for event in pygame.event.get():
                if event.type == QUIT:
                    exit = True
                elif event.type == KEYDOWN:
                    if event.key == K_ESCAPE:
                        exit = True
                    if event.key == K_SPACE:
                        SimpleUI.state['mode'] = 'move'
                    if event.key == K_LSHIFT:
                        SimpleUI.state['mode'] = 'prop'
                elif event.type == KEYUP:
                    if event.key == K_SPACE:
                        SimpleUI.state['mode'] = 'none'
                    if event.key == K_LSHIFT:
                        SimpleUI.state['mode'] = 'none'
                        #SimpleUI.state['mode'] = SimpleUI.state['old']
                elif event.type == MOUSEBUTTONDOWN:
                    self.handleMouseDown(pygame.mouse.get_pos())
                elif event.type == MOUSEBUTTONUP:
                    self.handleMouseUp(pygame.mouse.get_pos())
                elif event.type == MOUSEMOTION:
                    self.handleMouseMotion(pygame.mouse.get_pos())
            return exit

    def handleMouseDown(self, (x, y)):
        but = False
        SimpleUI.state['clicked'] = True
        for button in self.buttons:
            if (button.containsPoint(x, y)):
                button.do()
                SimpleUI.state['clicked'] = False
                but = True
                break
        for platform in self.gameRects:
            if platform.rect.collidepoint(x, y):
                self.selected = platform.rect
                if SimpleUI.state['mode'] is not 'prop': SimpleUI.state['mode'] = 'drag'
                else:
                    platform.setBy = eg.enterbox('ButtonGroup to that sets this platform')
                    print "set by"+platform.setBy+"yo"
                    if eg.boolbox("Platform visible by default?", "One more thing", ["Yes", "No"]):
                        platform.visibleDefault = True
                    else:
                        platform.visibleDefault = False
                but = True
                break    
        for gbutton in self.gameButtons:
            if gbutton.rect.collidepoint(x,y):
                self.selected = gbutton.rect
                if SimpleUI.state['mode'] is not 'prop': SimpleUI.state['mode'] = 'drag'
                else:
                    gbutton.sets = eg.enterbox('Object Group to set')
                but = True
        for recorder in self.gameRecorders:
            if recorder.rect.collidepoint(x,y):
                self.selected = recorder.rect
                SimpleUI.state['mode'] = 'drag'
                but = True
        if self.gamePlayer.rect.collidepoint(x,y):
            self.selected = self.gamePlayer.rect
            SimpleUI.state['mode'] = 'drag'
            but = True
        elif self.gameExit.rect.collidepoint(x,y):
            self.selected = self.gameExit.rect
            SimpleUI.state['mode'] = 'drag'
            but = True
        if not but:
            if SimpleUI.state['mode'] == 'rect':
                SimpleUI.LAST = [x,y]
            if SimpleUI.state['mode'] == 'player':
                self.gamePlayer.rect.center = (x,y)
            if SimpleUI.state['mode'] == 'recorder':
                temp = levelformat.Recorder(x,y)
                temp.rect.center = (x,y)
                self.gameRecorders.append(temp)
            if SimpleUI.state['mode'] == 'button':
                temp = levelformat.Button(x,y)
                temp.rect.center = (x,y)
                self.gameButtons.append(temp)
            if SimpleUI.state['mode'] == 'exit':
                self.gameExit.rect.center = (x,y)
            if SimpleUI.state['mode'] == 'move':
                SimpleUI.LAST = [x,y]
                             
    def handleMouseUp(self, (x,y)):
        if SimpleUI.state['mode'] == 'rect' and SimpleUI.state['clicked'] is True:
           width, height = [a-b for a,b in zip([x,y], SimpleUI.LAST)]
           temp = levelformat.Platform(SimpleUI.LAST[0], SimpleUI.LAST[1], width, height)
           if temp.rect.width > 30 and temp.rect.height > 30:
                self.gameRects.append(temp)
        SimpleUI.state['clicked'] = False
        self.selected = None

    def handleMouseMotion(self, (x,y)):
        if SimpleUI.state['clicked'] is True: 
            xo,yo = [a-b for a,b in zip([x,y], SimpleUI.CURRENT)]
            if SimpleUI.state['mode'] is 'move':
                self.moveEverything((xo,yo))
            elif SimpleUI.state['mode'] is 'drag' and self.selected is not None:
                self.moveSomething((xo,yo))
        SimpleUI.CURRENT = [x,y]
            
    def moveSomething(self, (x,y)):
        self.selected.move_ip(x,y)

    def moveEverything(self, (x,y)):
        for gbutton in self.gameButtons:
            gbutton.rect.move_ip(x,y)
        for recorder in self.gameRecorders:
            recorder.rect.move_ip(x,y)
        for lev in self.gameRects:
            lev.rect.move_ip(x,y)
        self.gamePlayer.rect.move_ip(x,y)
        self.gameExit.rect.move_ip(x,y)

    def draw(self):
        self.screen.fill(SimpleUI.WHITE)
        if SimpleUI.state['mode'] == 'rect' and SimpleUI.state['clicked'] is True:
            width = SimpleUI.CURRENT[0] - SimpleUI.LAST[0]
            height = SimpleUI.CURRENT[1] - SimpleUI.LAST[1]
            temp = pygame.Rect(SimpleUI.LAST, (width, height))
            temp.normalize()
            if temp.width > 30 and temp.height > 30:
                pygame.draw.rect(self.screen, SimpleUI.GREEN, temp)
            else:
                pygame.draw.rect(self.screen, SimpleUI.RED, temp)
        for gbutton in self.gameButtons:
            gbutton.draw(self.screen)
        for recorder in self.gameRecorders:
            recorder.draw(self.screen)
        self.gamePlayer.draw(self.screen)
        self.gameExit.draw(self.screen)
        for lev in self.gameRects:
            lev.draw(self.screen)
        for button in self.buttons:
            button.draw(self.screen)
        pygame.display.update()
            
# Start the program
if __name__ == '__main__':
    SimpleUI().run()
