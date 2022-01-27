--[[
    Horizontal aligns: LEFT, CENTER, RIGHT
    Vertical aligns: TOP, CENTER, BOTTOM
    Border / Padding: left, right, top, bottom
    Color: red, green, blue, alpha

    Minecraft colors:
    BLACK = ( 0, 0, 0, 255 )
    DARK_BLUE = ( 0, 0, 170, 255 )
    DARK_GREEN = ( 0, 170, 0, 255 )
    DARK_AQUA = ( 0, 170, 170, 255 )
    DARK_RED = ( 170, 0, 0, 255 )
    DARK_PURPLE = ( 170, 0, 170, 255 )
    GOLD = ( 255, 170, 0, 255 )
    GRAY = ( 170, 170, 170, 255 )
    DARK_GRAY = ( 85, 85, 85, 255 )
    BLUE = ( 85, 85, 255, 255 )
    GREEN = ( 85, 255, 85, 255 )
    AQUA = ( 85, 255, 255, 255 )
    RED = ( 255, 85, 85, 255 )
    LIGHT_PURPLE = ( 255, 85, 255, 255 )
    YELLOW = ( 255, 255, 85, 255 )
    WHITE = ( 255, 255, 255, 255 )
]]--

-- change these values if you just want to customize the color
local red = 100
local green = 0
local blue = 150

-- rename this function to match the file name
function Default(window, button, valueButton)
    -- Window

    window:setWidth( 110.0 )
    window:setPadding( 1.0, 0.0, 1.0, 0.0 )
    window:setPaddingColor( 15, 15, 15, 200 )

    window:setTitleHeight( 14.0 )
    window:setTitleColor( red, green, blue, 255 )

    window:setBorder( 2.0, 2.0, 2.0, 2.0 )
    window:setBorderColor( red, green, blue, 200 )

    window:getText():setPadding( 2.0, 0.0, 0.0, 0.0 )
    window:getText():setColor( 255, 255, 255, 255 )
    window:getText():setShadow( true )
    window:getText():setHorizontal( "LEFT" )
    window:getText():setVertical( "CENTER" )

    -- Button

    button:setHeight( 13.0 )

    button:setPrimaryColor( red, green, blue, 200 )
    button:setSecondaryColor( 30, 30, 30, 200 )

    button:setBorder( 0.0, 0.0, 0.0, 1.0 )
    button:setBorderColor( 15, 15, 15, 200 )

    button:getPrimaryText():setPadding( 1.0, 0.0, 0.0, 0.0 )
    button:getPrimaryText():setColor( 255, 255, 255, 255 )
    button:getPrimaryText():setShadow( true )
    button:getPrimaryText():setHorizontal( "LEFT" )
    button:getPrimaryText():setVertical( "CENTER" )

    button:getSecondaryText():setPadding( 0.0, 2.0, 0.0, 0.0 )
    button:getSecondaryText():setColor( 170, 170, 170, 255 )
    button:getSecondaryText():setShadow( true )
    button:getSecondaryText():setHorizontal( "RIGHT" )
    button:getSecondaryText():setVertical( "CENTER" )

    -- Setting Button

    valueButton:setHeight( 12.0 )

    valueButton:setBorder( 2.0, 0.0, 0.0, 1.0 )
    valueButton:setBorderColor( 15, 15, 15, 200 )

end
