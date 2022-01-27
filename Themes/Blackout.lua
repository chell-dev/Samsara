-- Theme identical to Blackout 2.0
-- Author: chell

local red = 200
local green = 50
local blue = 200

function Blackout(window, button, valueButton)

    -- Window

    window:setWidth( 90.0 )
    window:setPadding( 0.0, 0.0, 1.0, 0.0 )
    window:setPaddingColor( 10, 10, 10, 200 )

    window:setTitleHeight( 13.0 )
    window:setTitleColor( red, green, blue, 255 )

    window:setBorder( 2.0, 2.0, 2.0, 2.0 )
    window:setBorderColor( red, green, blue, 255 )

    window:getText():setPadding( 2.0, 0.0, 2.0, 0.0 )
    window:getText():setColor( 255, 255, 255, 255 )
    window:getText():setShadow( true )
    window:getText():setHorizontal( "LEFT" )
    window:getText():setVertical( "TOP" )

    -- Button

    button:setHeight( 13.0 )

    button:setPrimaryColor( red, green, blue, 255 )
    button:setSecondaryColor( 50, 50, 50, 200 )

    button:setBorder( 0.0, 0.0, 0.0, 1.0 )
    button:setBorderColor( 10, 10, 10, 200 )

    button:getPrimaryText():setPadding( 2.0, 0.0, 2.0, 0.0 )
    button:getPrimaryText():setColor( 255, 255, 255, 255 )
    button:getPrimaryText():setShadow( true )
    button:getPrimaryText():setHorizontal( "LEFT" )
    button:getPrimaryText():setVertical( "TOP" )

    button:getSecondaryText():setPadding( 0.0, 2.0, 2.0, 0.0 )
    button:getSecondaryText():setColor( 170, 170, 170, 255 )
    button:getSecondaryText():setShadow( true )
    button:getSecondaryText():setHorizontal( "RIGHT" )
    button:getSecondaryText():setVertical( "TOP" )

    -- ValueButton

    valueButton:setHeight( 13.0 )

    valueButton:setBorder( 2.0, 0.0, 0.0, 1.0 )
    valueButton:setBorderColor( 10, 10, 10, 200 )

end
