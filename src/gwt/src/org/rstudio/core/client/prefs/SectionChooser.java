/*
 * SectionChooser.java
 *
 * Copyright (C) 2009-19 by RStudio, Inc.
 *
 * Unless you have received this program directly from RStudio pursuant
 * to the terms of a commercial license agreement with RStudio, then
 * this program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
 *
 */
package org.rstudio.core.client.prefs;

import com.google.gwt.aria.client.Roles;
import org.rstudio.core.client.ElementIds;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.rstudio.core.client.a11y.A11y;

class SectionChooser extends SimplePanel implements
                                                HasSelectionHandlers<Integer>
{
   private class ClickableVerticalPanel extends VerticalPanel
      implements HasClickHandlers
   {

      public HandlerRegistration addClickHandler(ClickHandler handler)
      {
         return addDomHandler(handler, ClickEvent.getType());
      }
   }

   public SectionChooser()
   {
      setStyleName(res_.styles().sectionChooser());
      inner_.setStyleName(res_.styles().sectionChooserInner());
      setWidget(inner_);
      Roles.getTablistRole().set(getElement());
      A11y.setARIATablistOrientation(getElement(), true /*vertical*/);
   }

   public void addSection(ImageResource icon, String name)
   {
      Image img = new Image(icon.getSafeUri());
      nudgeDown(img);
      A11y.setDecorativeImage(img.getElement());
      img.setSize("29px", "20px");
      Label label = new Label(name, false);
      final ClickableVerticalPanel panel = new ClickableVerticalPanel();
      panel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
      final HorizontalPanel innerPanel = new HorizontalPanel();
      innerPanel.setWidth("0px");
      innerPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
      innerPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
      innerPanel.add(img);
      innerPanel.add(nudgeRightPlus(label));
      panel.add(innerPanel);
      panel.setStyleName(res_.styles().section());
      panel.getElement().setId(ElementIds.idFromLabel(name) + "_options");

      panel.addClickHandler(new ClickHandler()
      {
         public void onClick(ClickEvent event)
         {
            select(inner_.getWidgetIndex(panel));
         }
      });

      Roles.getTabRole().set(panel.getElement());
      inner_.add(panel);
   }

   public void select(Integer index)
   {
      if (selectedIndex_ != null)
      {
         Widget prevItem = inner_.getWidget(selectedIndex_);
         prevItem.removeStyleName(res_.styles().activeSection());
         prevItem.getElement().setTabIndex(-1);
      }

      selectedIndex_ = index;

      if (index != null)
      {
         Widget newItem = inner_.getWidget(index);
         newItem.addStyleName(res_.styles().activeSection());
         newItem.getElement().setTabIndex(0);
      }

      SelectionEvent.fire(this, index);
   }
   
   public void hideSection(Integer index)
   {
      if (index != null)
         inner_.getWidget(index).setVisible(false);
   }

   public HandlerRegistration addSelectionHandler(SelectionHandler<Integer> handler)
   {
      return addHandler(handler, SelectionEvent.getType());
   }

   public int getDesiredWidth()
   {
      return 122;
   }

   public void focus()
   {
      if (selectedIndex_ != null)
      {
         Widget currentItem = inner_.getWidget(selectedIndex_);
         currentItem.getElement().focus();
      }
   }

   private Widget nudgeRightPlus(Widget widget)
   {
      widget.addStyleName(res_.styles().nudgeRightPlus());
      return widget;
   }

   private Widget nudgeDown(Widget widget)
   {
      widget.addStyleName(res_.styles().nudgeDown());
      return widget;
   }

   private Integer selectedIndex_;
   private final VerticalPanel inner_ = new VerticalPanel();
   private static final PreferencesDialogBaseResources res_ = 
                                    PreferencesDialogBaseResources.INSTANCE;
}
