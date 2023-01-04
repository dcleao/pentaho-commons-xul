/*!
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2002-2023 Hitachi Vantara..  All rights reserved.
 */

package org.pentaho.ui.xul.gwt.util;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.pentaho.gwt.widgets.client.dialogs.DialogBox;
import org.pentaho.ui.xul.gwt.AbstractGwtXulContainer;

public abstract class GenericDialog extends AbstractGwtXulContainer {

  protected DialogBox dialog;
  private VerticalPanel contents = new VerticalPanel();
  private String title = "";

  public static final int CANCEL = 0;
  public static final int ACCEPT = 1;
  public static final int EXTRA1 = 2;
  public static final int EXTRA2 = 3;

  // requested height is adjusted by this value.
  private static final int HEADER_HEIGHT = 32;

  public GenericDialog( String tagName ) {
    super( tagName );

    // Default ARIA role.
    setAriaRole( "dialog" );
  }

  private void createDialog() {
    dialog = new DialogBox( false, true ) {
      @Override
      protected void onEnter() {
        // Do not close the dialog on ENTER preview, by default.
      }
    };
    dialog.addStyleName( "pentaho-gwt-xul" );
    dialog.setWidget( contents );
  }

  public void hide() {
    if ( dialog != null ) {
      dialog.hide();
    }
  }

  public void show() {
    // Instantiation if delayed to prevent errors with the underlying GWT's not being able to calculate available
    // size
    // in the case that the GWT app has been loaded into an iframe that's not visible.
    if ( dialog == null ) {
      createDialog();
    }
    dialog.setText( title );
    updateDomAriaRole( dialog );

    contents.clear();

    VerticalPanel bodyPanel = new VerticalPanel();
    bodyPanel.setStyleName( "dialog" );
    bodyPanel.setWidth( "100%" );
    bodyPanel.setSpacing( 0 );
    bodyPanel.setHeight( "100%" );

    Panel dialogContents = getDialogContents();
    dialogContents.setSize( "100%", "100%" );
    dialogContents.setStyleName( "dialog-content" );

    bodyPanel.add( dialogContents );
    bodyPanel.setCellHeight( dialogContents, "100%" );

    contents.add( bodyPanel );
    contents.setCellHeight( bodyPanel, "100%" );

    if ( getBgcolor() != null ) {
      dialogContents.getElement().getStyle().setProperty( "backgroundColor", getBgcolor() );
    }

    Panel buttonPanel = this.getButtonPanel();
    buttonPanel.setWidth( "100%" );

    HorizontalPanel buttonPanelWrapper = new HorizontalPanel();
    buttonPanelWrapper.setStyleName( "button-panel" );
    buttonPanelWrapper.add( buttonPanel );
    buttonPanelWrapper.setWidth( "100%" );
    buttonPanelWrapper.setCellWidth( buttonPanel, "100%" );

    contents.add( buttonPanelWrapper );

    contents.setWidth( "100%" );
    contents.setHeight( "100%" );

    if ( getWidth() > 0 ) {
      contents.setWidth( getWidth() + "px" );
    }
    if ( getHeight() > 0 ) {
      int offsetHeight = getHeight() - HEADER_HEIGHT;
      contents.setHeight( offsetHeight + "px" );
    }

    dialog.center();
  }

  public Panel getDialogContents() {
    return null;
  }

  public Panel getButtonPanel() {
    return null;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle( final String title ) {
    this.title = title;
  }

  public boolean isHidden() {
    return dialog == null || !dialog.isVisible();
  }

  public boolean isVisible() {
    return !isHidden();
  }

}
